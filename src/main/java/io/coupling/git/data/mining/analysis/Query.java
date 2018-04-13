package io.coupling.git.data.mining.analysis;

public class Query {

  private static final String query = "MATCH (file:ChangedFile)-[r:CHANGED_TOGETHER_WITH]->()\n"
      + "WHERE file.path ENDS WITH '.java' AND r.timestamp>='2017-01-01'\n"
      + "WITH file, count(r) as togethersCount\n"
      + "ORDER BY togethersCount DESC\n"
      + "RETURN file, togethersCount LIMIT 10";
}
