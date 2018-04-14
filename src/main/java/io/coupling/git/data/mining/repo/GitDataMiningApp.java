package io.coupling.git.data.mining.repo;

import io.coupling.git.data.mining.analysis.CoupledPairs;
import io.coupling.git.data.mining.analysis.PersistedCommit;
import java.time.Duration;
import java.time.Instant;
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

public class GitDataMiningApp {

  public static void main(String[] args) {
    final String uri = "bolt://localhost:7687";
    final String user = "neo4j";
    final String pass = "root";
    try (final Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass))) {
      try (final Session session = driver.session()) {
        commits().map(commit -> new PersistedCommit(session, commit))
            .forEach(PersistedCommit::persist);
        new CoupledPairs(session).query(10, Instant.now().minus(Duration.ofDays(30)));
      }
    }
  }

  private static Stream<Commit> commits() {
    final Repository repository = new RepoFactory().get("../sonarlint-intellij/.git");
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
