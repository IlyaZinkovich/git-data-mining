package io.coupling.git.data.mining.analysis;

import io.coupling.git.data.mining.repo.Commit;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;

public class GraphCommitRepository {

  private static final String PERSIST_CHANGED_FILE_REQUEST =
      "MERGE (file:ChangedFile {path:$path})";
  private static final String PERSIST_CHANGED_FILES_RELATIONS_REQUEST =
      "MATCH (firstFile:ChangedFile) WHERE firstFile.path=$firstPath\n"
          + "MATCH (secondFile:ChangedFile) WHERE secondFile.path=$secondPath\n"
          + "CREATE (firstFile)-[:CHANGED_TOGETHER_WITH {timestamp:$timestamp}]->(secondFile)\n";

  private final Session session;

  public GraphCommitRepository(final Session session) {
    this.session = session;
  }

  public void persist(final Commit commit) {
    persistChangedFiles(commit);
    persistChangedFilesRelations(commit);
  }

  private void persistChangedFiles(final Commit commit) {
    commit.toChangedFilesParameters(path -> path.endsWith(".java"))
        .map(parameters -> new Statement(PERSIST_CHANGED_FILE_REQUEST, parameters))
        .forEach(statement -> session.writeTransaction(transaction -> transaction.run(statement)));
  }

  private void persistChangedFilesRelations(final Commit commit) {
    commit.toChangedFileRelationsParameters()
        .map(parameters -> new Statement(PERSIST_CHANGED_FILES_RELATIONS_REQUEST, parameters))
        .forEach(statement -> session.writeTransaction(transaction -> transaction.run(statement)));
  }
}
