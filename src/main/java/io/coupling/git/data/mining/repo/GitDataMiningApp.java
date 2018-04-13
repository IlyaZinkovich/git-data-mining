package io.coupling.git.data.mining.repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;

public class GitDataMiningApp {

  public static void main(String[] args) {
    final Repository repository = new RepoFactory().get("../hot-git/.git");
    final Git git = new Git(repository);
    try (final ObjectReader objectReader = repository.newObjectReader()) {
      final CommitTreeParserFactory parserFactory = new CommitTreeParserFactory(objectReader);
      new Commits(git, parserFactory).stream().forEach(System.out::println);
    }
//    final String uri = "bolt://localhost:7687";
//    final String user = "neo4j";
//    final String pass = "root";
//    try (final Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass))) {
//      try (final Session session = driver.session()) {
//        final Map<String, Object> parameters = new HashMap<>();
//        final String persistClientRequest = "request";
//        final Statement persistClientRequestStatement = new Statement(persistClientRequest,
//            parameters);
//        session.writeTransaction(transaction -> transaction.run(persistClientRequestStatement));
//      }
//    }
  }
}
