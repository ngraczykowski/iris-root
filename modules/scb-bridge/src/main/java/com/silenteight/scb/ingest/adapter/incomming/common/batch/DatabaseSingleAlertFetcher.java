package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
public class DatabaseSingleAlertFetcher implements SingleAlertFetcher {

  private final RecordDecisionsFetcher decisionsFetcher;
  private final RecordCompositeFetcher recordCompositeFetcher;
  private final DataSource externalDataSource;

  @Override
  public Optional<Alert> fetch(String systemId) {
    try {
      return tryFetch(systemId);
    } catch (SQLException e) {
      log.error("Cannot fetch an alert", e);
      throw new AlertFetcherException("Could not fetch alert " + systemId, e);
    }
  }

  private Optional<Alert> tryFetch(String systemId) throws SQLException {
    log.debug("Fetching alert");

    try (Connection connection = externalDataSource.getConnection()) {
      List<Decision> decisions = decisionsFetcher.fetchDecisions(connection, systemId);
      Optional<AlertComposite> alertComposites = recordCompositeFetcher.fetchRecordWithDetails(
          connection, decisions, systemId);
      return alertComposites.map(AlertComposite::getAlert);
    }
  }

  private static class AlertFetcherException extends RuntimeException {

    private static final long serialVersionUID = -6189328487454637851L;

    AlertFetcherException(String message, SQLException cause) {
      super(message, cause);
    }
  }
}
