package io.coupling.git.data.mining.analysis;

import static java.lang.String.format;

import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.util.Map;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoupledPairs {

  private static final Logger log = LoggerFactory.getLogger(CoupledPairs.class);

  private static final String TOP_COUPLED_FILE_CHANGES_REQUEST =
      "MATCH (file:ChangedFile)-[r:CHANGED_TOGETHER_WITH]-(anotherFile:ChangedFile)\n"
          + "WHERE r.timestamp>=$timestamp\n"
          + "RETURN file, anotherFile, count(r) as relationsCount\n"
          + "ORDER BY relationsCount DESC LIMIT $limit";

  private final Session session;

  public CoupledPairs(final Session session) {
    this.session = session;
  }

  public void query(final int limit, final Instant since) {
    final Map<String, Object> params = ImmutableMap.<String, Object>builder()
        .put("limit", limit)
        .put("timestamp", since.toString())
        .build();
    final Statement statement = new Statement(TOP_COUPLED_FILE_CHANGES_REQUEST, params);
    final StatementResult result =
        session.readTransaction(transaction -> transaction.run(statement));
    result.forEachRemaining(record -> log.debug(recordToString(record)));
  }

  private String recordToString(final Record record) {
    return format("%s\t%d\t%s",
        filePath(record, "file"),
        record.get("relationsCount").asInt(),
        filePath(record, "anotherFile")
    );
  }

  private String filePath(final Record record, final String nodeName) {
    return record.get(nodeName).get("path").asString();
  }
}
