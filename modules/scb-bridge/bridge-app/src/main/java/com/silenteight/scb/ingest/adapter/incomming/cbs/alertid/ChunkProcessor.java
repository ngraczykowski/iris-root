package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.StopWatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
class ChunkProcessor {

  private static final String QUERY_PREFIX = "SELECT SYSTEM_ID, BATCH_ID FROM ";
  private final Consumer<AlertIdCollection> consumer;

  void process(Statement statement, AlertIdContext context) throws SQLException {
    var processor = new Processor(context);

    String query = prepareQuery(context.getRecordsView());
    var stopWatch = StopWatch.createStarted();
    log.info("Executing query for {} ...", context);
    int totalProcessed = 0;
    try (ResultSet resultSet = statement.executeQuery(query)) {
      log.info("Query executed in: {}, processing ResultSet ...", stopWatch);
      while (resultSet.next() && !numberToReadReached(context, totalProcessed)) {
        processor.process(resultSet);
        totalProcessed++;
        if (totalProcessed % context.getChunkSize() == 0) {
          log.info("Query processed {} records so far in {}", totalProcessed, stopWatch);
        }
      }
      log.info("Query finished, total records processed: {} executed in: {}",
          totalProcessed, stopWatch);
    }

    processor.processRemaining();
  }

  private boolean numberToReadReached(AlertIdContext context, int totalProcessed) {
    return context.getTotalRecordsToRead() > 0 && totalProcessed == context.getTotalRecordsToRead();
  }

  private static String prepareQuery(String viewName) {
    return QUERY_PREFIX + viewName;
  }

  class Processor {

    private final AlertIdContext context;
    private final List<AlertId> alertsToBeProcessed = new ArrayList<>();

    Processor(AlertIdContext alertIdContext) {
      context = alertIdContext;
    }

    void process(ResultSet resultSet) throws SQLException {
      alertsToBeProcessed.add(map(resultSet));

      if (alertsToBeProcessed.size() >= context.getChunkSize())
        sendAlertIds();
    }

    private void sendAlertIds() {
      log.info("Publishing collected {} alerts to be processed", alertsToBeProcessed.size());
      consumer.accept(new AlertIdCollection(alertsToBeProcessed, context));
      alertsToBeProcessed.clear();
    }

    private AlertId map(@NonNull ResultSet resultSet) throws SQLException {
      return AlertId.builder()
          .systemId(resultSet.getString("SYSTEM_ID"))
          .batchId(resultSet.getString("BATCH_ID"))
          .build();
    }

    void processRemaining() {
      if (!alertsToBeProcessed.isEmpty())
        sendAlertIds();
    }
  }
}