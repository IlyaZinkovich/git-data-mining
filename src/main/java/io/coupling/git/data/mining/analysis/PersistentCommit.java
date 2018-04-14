package io.coupling.git.data.mining.analysis;

import io.coupling.git.data.mining.repo.commits.ChangesPerCommit;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;

public class PersistentCommit {

  private static final String PERSIST_CHANGED_FILE_REQUEST =
      "MERGE (file:ChangedFile {path:$path})";
  private static final String PERSIST_CHANGED_FILES_RELATIONS_REQUEST =
      "MATCH (firstFile:ChangedFile) WHERE firstFile.path=$firstPath\n"
          + "MATCH (secondFile:ChangedFile) WHERE secondFile.path=$secondPath\n"
          + "CREATE (firstFile)-[:CHANGED_TOGETHER_WITH {timestamp:$timestamp}]->(secondFile)\n";

  private final Session session;
  private final ChangesPerCommit commit;

  public PersistentCommit(final Session session, final ChangesPerCommit commit) {
    this.session = session;
    this.commit = commit;
  }

  public void persist() {
    persistChangedFiles();
    persistChangedFilesRelations();
  }

  private void persistChangedFiles() {
    commit.toChangedFilesParameters(path -> path.endsWith(".java"))
        .map(parameters -> new Statement(PERSIST_CHANGED_FILE_REQUEST, parameters))
        .forEach(statement -> session.writeTransaction(transaction -> transaction.run(statement)));
  }

  private void persistChangedFilesRelations() {
    commit.toChangedFileRelationsParameters()
        .map(parameters -> new Statement(PERSIST_CHANGED_FILES_RELATIONS_REQUEST, parameters))
        .forEach(statement -> session.writeTransaction(transaction -> transaction.run(statement)));
  }
}
