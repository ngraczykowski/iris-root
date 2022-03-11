package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Slf4j
abstract class BaseMultipleAlertCompositeFetcher implements MultipleAlertCompositeFetcher {

  private final RecordDecisionsFetcher decisionsFetcher;
  private final DataSource externalDataSource;

  @Override
  public List<AlertComposite> fetch(List<String> systemIds) {
    try {
      return fetchInTransaction(systemIds);
    } catch (SQLException e) {
      log.error("Cannot fetch AlertComposite data!", e);
      return emptyList();
    }
  }

  private List<AlertComposite> fetchInTransaction(List<String> systemIds) throws SQLException {
    List<AlertComposite> result = new ArrayList<>();
    try (Connection connection = externalDataSource.getConnection()) {
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      result.addAll(fetch(connection, systemIds));

      connection.commit();
    }
    return result;
  }

  protected abstract List<AlertComposite> fetch(Connection connection, List<String> systemIds)
      throws SQLException;

  Map<String, List<Decision>> fetchDecisions(
      Connection connection, List<String> systemIds) throws SQLException {
    return decisionsFetcher.fetchDecisions(connection, systemIds);
  }
}
