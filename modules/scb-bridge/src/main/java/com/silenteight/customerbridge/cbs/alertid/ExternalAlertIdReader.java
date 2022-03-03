package com.silenteight.customerbridge.cbs.alertid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

@Slf4j
@RequiredArgsConstructor
public class ExternalAlertIdReader {

  private final ChunkProcessor chunkProcessor;
  private final JdbcTemplate jdbcTemplate;
  private final int timeout;

  static final int MAX_CHUNK_SIZE = 1_000;

  public void read(AlertIdContext context) {
    try {
      jdbcTemplate.execute((ConnectionCallback<?>) con -> {
        con.setAutoCommit(false);
        readInChunksAndSendForProcessing(con, context);
        return null;
      });
    } catch (RuntimeException ex) {
      log.error("Cannot read systemId / batchId for processing", ex);
    }
  }

  private void readInChunksAndSendForProcessing(
      Connection connection, AlertIdContext context) throws SQLException {
    try (Statement statement = connection.createStatement(TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
      statement.setFetchSize(MAX_CHUNK_SIZE);
      statement.setQueryTimeout(timeout);

      chunkProcessor.process(statement, context);
    }
  }
}
