/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.StopWatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
class ChunkProcessor {

  private static final String QUERY_PREFIX = "SELECT SYSTEM_ID, BATCH_ID FROM ";
  private final Consumer<AlertIdCollection> consumer;

  void process(Statement statement, AlertIdReaderContext context) throws SQLException {
    var alertIdContext = context.alertIdContext();

    var processor = new CbsAlertIdProcessor(alertIdContext, context.chunkSize(), consumer);

    String query = prepareQuery(alertIdContext.getRecordsView());
    var stopWatch = StopWatch.createStarted();
    log.info("Executing query for {} ...", context);
    int totalProcessed = 0;
    try (ResultSet resultSet = statement.executeQuery(query)) {
      log.info("Query executed in: {}, processing ResultSet ...", stopWatch);
      while (resultSet.next() && !numberToReadReached(context, totalProcessed)) {
        processor.process(map(resultSet));
        totalProcessed++;
        if (totalProcessed % context.chunkSize() == 0) {
          log.info("Query processed {} records so far in {}", totalProcessed, stopWatch);
        }
      }
      log.info("Query finished, total records processed: {} executed in: {}",
          totalProcessed, stopWatch);
    }

    processor.processRemaining();
  }

  private boolean numberToReadReached(AlertIdReaderContext context, int totalProcessed) {
    return context.totalRecordsToRead() > 0 && totalProcessed == context.totalRecordsToRead();
  }

  private static String prepareQuery(String viewName) {
    return QUERY_PREFIX + viewName;
  }

  private AlertId map(@NonNull ResultSet resultSet) throws SQLException {
    return new AlertId(
        resultSet.getString("SYSTEM_ID"),
        resultSet.getString("BATCH_ID"));
  }

}
