package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
class ChunkProcessor {

  private final Consumer<AlertIdCollection> consumer;

  private static final String QUERY_PREFIX = "SELECT SYSTEM_ID, BATCH_ID FROM ";

  void process(Statement statement, AlertIdContext context) throws SQLException {
    var processor = new Processor(context);

    String query = prepareQuery(context.getRecordsView());
    try (ResultSet resultSet = statement.executeQuery(query)) {
      while (resultSet.next()) {
        processor.process(resultSet);
      }
    }

    processor.processRemaining();
  }

  private static String prepareQuery(String viewName) {
    return QUERY_PREFIX + viewName;
  }

  class Processor {

    private AlertIdContext context;
    private final List<AlertId> alertsToBeProcessed = new ArrayList<>();

    Processor(AlertIdContext alertIdContext) {
      context = alertIdContext;
    }

    void process(ResultSet resultSet) throws SQLException {
      alertsToBeProcessed.add(map(resultSet));

      if (alertsToBeProcessed.size() >= ExternalAlertIdReader.MAX_CHUNK_SIZE)
        sendAlertIds();
    }

    void processRemaining() {
      if (!alertsToBeProcessed.isEmpty())
        sendAlertIds();
    }

    private void sendAlertIds() {
      consumer.accept(new AlertIdCollection(alertsToBeProcessed, context));
      alertsToBeProcessed.clear();
    }

    private AlertId map(@NonNull ResultSet resultSet) throws SQLException {
      return AlertId.builder()
          .systemId(resultSet.getString("SYSTEM_ID"))
          .batchId(resultSet.getString("BATCH_ID"))
          .build();
    }
  }
}
