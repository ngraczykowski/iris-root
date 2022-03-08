package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.Decision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm.ExternalId.HIT_UNIQUE_ID;
import static com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm.ExternalId.SYSTEM_ID;
import static com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm.ExternalId.tryToExtractWatchlistIdFromHitUniqueId;

@Slf4j
class EcmRecordDecisionsFetcher {

  private static final int DECISIONS_PER_RECORD = 1_000;
  private static final Pattern WHITESPACE = Pattern.compile("[\\t\\n\\r]+");

  private final EcmDecisionRowMapper decisionRowMapper;
  private final String ecmViewName;
  private String fetchSqlQuery;

  EcmRecordDecisionsFetcher(EcmDecisionRowMapper decisionRowMapper, String ecmViewName) {
    this.decisionRowMapper = decisionRowMapper;
    this.ecmViewName = ecmViewName;

    this.fetchSqlQuery = prepareFetchQuery();
  }

  @SuppressWarnings("findsecbugs:SQL_INJECTION_JDBC")
  public Map<ExternalId, List<Decision>> fetchDecisions(
      Connection connection, List<ExternalId> externalIds)
      throws SQLException {

    if (log.isDebugEnabled())
      log.debug("Fetching decisions: systemIds={}, ecmViewName={}", externalIds, ecmViewName);

    Map<ExternalId, List<Decision>> result = new HashMap<>();
    String query = prepareQuery(externalIds);

    if (log.isTraceEnabled())
      log.trace("Executing SQL: decisionsPerRecord={}, query={}", DECISIONS_PER_RECORD, query);

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setFetchSize(externalIds.size() * DECISIONS_PER_RECORD);
      setQueryParameters(statement, externalIds);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          var systemId = resultSet.getString(SYSTEM_ID);
          var watchlistId =
              tryToExtractWatchlistIdFromHitUniqueId(resultSet.getString(HIT_UNIQUE_ID));

          if (externalIds.contains(new ExternalId(systemId, watchlistId))) {
            Decision decision = decisionRowMapper.mapRow(resultSet);
            addDecision(decision, systemId, watchlistId, result);
          }
        }
      }
    }

    return result;
  }

  private String prepareQuery(List<ExternalId> externalIds) {
    return String.format(
        fetchSqlQuery, String.join(",", Collections.nCopies(externalIds.size(), "?")));
  }

  private String prepareFetchQuery() {
    if (log.isTraceEnabled()) {
      log.trace("Executing SQL: ecmViewName={}, query={}", ecmViewName,
          WHITESPACE.matcher(EcmQueryTemplates.DECISIONS_QUERY).replaceAll(" "));
    }

    return EcmQueryTemplates.DECISIONS_QUERY
        .replace(":ecmViewName", ecmViewName);
  }

  private static void setQueryParameters(
      PreparedStatement stat, List<ExternalId> externalIds) throws
      SQLException {
    int systemIdIdx = 1;
    final List<String> systemIds =
        externalIds.stream().map(ExternalId::getSystemId).collect(Collectors.toList());
    for (String systemId : systemIds) {
      stat.setString(systemIdIdx, systemId);
      systemIdIdx++;
    }
  }

  private static void addDecision(
      Decision decision, String systemId, String watchlistId,
      Map<ExternalId, List<Decision>> decisions) {

    if (log.isTraceEnabled())
      log.trace("Adding decision: decision={}, count={}", decision.getId(), decisions.size());

    decisions
        .computeIfAbsent(new ExternalId(systemId, watchlistId), s -> new ArrayList<>())
        .add(decision);
  }
}
