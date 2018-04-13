package io.coupling.git.data.mining.analysis;

public class Query {

  private static final String query =
      "MATCH (file:ChangedFile)-[r:CHANGED_TOGETHER_WITH]->(anotherFile:ChangedFile)\n"
          + "WHERE file.path ENDS WITH '.java'\n"
          + "AND anotherFile.path ENDS WITH '.java'\n"
          + "AND r.timestamp>='2017-01-01'\n"
          + "WITH file, count(r) as togethersCount\n"
          + "ORDER BY togethersCount DESC\n"
          + "RETURN file, togethersCount LIMIT 10";
  private static final String queryPairs =
      "MATCH (file:ChangedFile)-[r:CHANGED_TOGETHER_WITH]-(anotherFile:ChangedFile)\n"
          + "WHERE file.path ENDS WITH '.java'\n"
          + "AND anotherFile.path ENDS WITH '.java'\n"
          + "AND r.timestamp>='2018-01-01'\n"
          + "RETURN file, count(r) as togethersCount, anotherFile\n"
          + "ORDER BY togethersCount DESC LIMIT 3";
}
