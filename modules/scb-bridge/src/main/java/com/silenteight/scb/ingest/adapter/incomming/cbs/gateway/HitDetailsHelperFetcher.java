package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration;
import com.silenteight.sep.base.aspects.metrics.Timed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

@Slf4j
class HitDetailsHelperFetcher implements CbsHitDetailsHelperFetcher {

  private static final int HITS_PER_RECORD = 1_000;
  private static final int SYSTEM_ID_PARAMETER_INDEX = 1;
  private static final int BATCH_ID_PARAMETER_INDEX = 2;

  private static final String CBS_HITS_DETAILS_QUERY =
      "SELECT SYSTEM_ID, BATCH_ID, SEQ_NO, HIT_NEO_FLAG"
          + " FROM :cbsHitsDetailsViewName"
          + " WHERE SYSTEM_ID = ? AND BATCH_ID = ? ORDER BY LPAD(SEQ_NO, 5, '0')";

  private final CbsHitDetailsRowMapper detailsRowMapper = new CbsHitDetailsRowMapper();

  private final String query;
  private final int queryTimeout;

  HitDetailsHelperFetcher(FetcherConfiguration configuration) {
    this.query = prepareQuery(configuration.getDbRelationName());
    this.queryTimeout = configuration.getTimeout();
  }

  @Timed(
      value = "serp.scb.bridge.oracle.fetch.cbs-hit-details.time",
      description = "Time taken to fetch single CBS hit details")
  @Override
  public List<CbsHitDetails> fetch(Connection connection, String systemId, String batchId) {
    List<CbsHitDetails> result = new ArrayList<>();

    if (log.isTraceEnabled())
      log.trace("Executing SQL: hitsPerRecord={}, query={}", HITS_PER_RECORD, query);

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setFetchSize(HITS_PER_RECORD);
      statement.setString(SYSTEM_ID_PARAMETER_INDEX, systemId);
      statement.setString(BATCH_ID_PARAMETER_INDEX, batchId);
      statement.setQueryTimeout(queryTimeout);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          CbsHitDetails details = detailsRowMapper.mapRow(resultSet, 0);
          result.add(details);
        }
      }
    } catch (SQLException ex) {
      log.error(
          "Cannot fetch CbsHitDetails for system Id: {}, batch Id: {}", systemId, batchId, ex);
    }
    return result;
  }

  @Nonnull
  private static String prepareQuery(@NonNull String viewName) {
    return CBS_HITS_DETAILS_QUERY.replace(":cbsHitsDetailsViewName", viewName);
  }
}
