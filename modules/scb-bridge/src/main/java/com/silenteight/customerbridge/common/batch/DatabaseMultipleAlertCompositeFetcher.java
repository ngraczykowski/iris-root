package com.silenteight.customerbridge.common.batch;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

@Slf4j
class DatabaseMultipleAlertCompositeFetcher extends BaseMultipleAlertCompositeFetcher {

  private final RecordCompositeFetcher recordCompositeFetcher;

  DatabaseMultipleAlertCompositeFetcher(
      RecordDecisionsFetcher decisionsFetcher,
      DataSource externalDataSource,
      RecordCompositeFetcher recordCompositeFetcher) {
    super(decisionsFetcher, externalDataSource);
    this.recordCompositeFetcher = recordCompositeFetcher;
  }

  @Override
  protected List<AlertComposite> fetch(Connection connection, List<String> systemIds)
      throws SQLException {

    Map<String, List<Decision>> decisionsMap = fetchDecisions(connection, systemIds);
    return recordCompositeFetcher.fetchRecordsWithDetails(connection, decisionsMap, systemIds);
  }
}
