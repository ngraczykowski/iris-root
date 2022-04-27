package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

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

  public void read(AlertIdReaderContext context) {
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
      Connection connection, AlertIdReaderContext context) throws SQLException {
    try (Statement statement = connection.createStatement(TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
      statement.setFetchSize(context.chunkSize());
      statement.setQueryTimeout(timeout);

      chunkProcessor.process(statement, context);
    }
  }
}
