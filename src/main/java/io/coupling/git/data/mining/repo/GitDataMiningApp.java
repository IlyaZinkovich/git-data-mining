package io.coupling.git.data.mining.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;

public class GitDataMiningApp {

  public static void main(String[] args) {
    final String uri = "bolt://localhost:7687";
    final String user = "neo4j";
    final String pass = "root";
    try (final Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass))) {
      try (final Session session = driver.session()) {
        commits().forEach(commit -> {
          final Set<ChangedFile> changes = commit.changes();
          changes.forEach(changedFile -> {
            final String persistChangedFile = "MERGE (file:ChangedFile {path:$path})";
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("path", changedFile.path());
            final Statement persistChangedFileStatement = new Statement(persistChangedFile,
                parameters);
            session.writeTransaction(transaction -> transaction.run(persistChangedFileStatement));
          });
          changes.stream().findAny().ifPresent(changedFile -> {
            changes.stream().filter(change -> !changedFile.equals(change)).forEach(change -> {
              final String request =
                  "MATCH (firstFile:ChangedFile) WHERE firstFile.path=$firstPath\n"
                      + "MATCH (secondFile:ChangedFile) WHERE secondFile.path=$secondPath\n"
                      + "CREATE (firstFile)-[:CHANGED_TOGETHER_WITH {timestamp:$timestamp}]->(secondFile)\n";
              final Map<String, Object> parameters = new HashMap<>();
              parameters.put("firstPath", changedFile.path());
              parameters.put("secondPath", change.path());
              parameters.put("timestamp", commit.timestamp().toString());
              final Statement persistChangedFileStatement = new Statement(request, parameters);
              session.writeTransaction(transaction -> transaction.run(persistChangedFileStatement));
            });
          });
        });
      }
    }
  }

  private static Stream<Commit> commits() {
    final Repository repository = new RepoFactory().get("../careem/uid/.git");
    final Git git = new Git(repository);
    try (final ObjectReader objectReader = repository.newObjectReader()) {
      final CommitTreeParserFactory parserFactory = new CommitTreeParserFactory(objectReader);
      final GitLog gitLog = new GitLog(git);
      Function<RevCommit, DiffWithParent> revCommitTransformer =
          commit -> new DiffWithParent(commit, parserFactory, git);
      return new Commits(gitLog).stream(revCommitTransformer);
    }
  }
}
