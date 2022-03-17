package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.QueryStatementHelper;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Slf4j
@RequiredArgsConstructor
class RecordDecisionsFetcher implements DecisionFetcher {

  private static final int DECISIONS_PER_RECORD = 1_000;
  private static final String DECISIONS_QUERY =
      "SELECT D.SYSTEM_ID, D.OPERATOR, D.DECISION_DATE, D.TYPE, D.COMMENTS, S.STATE_NAME"
          + " FROM FFF_DECISIONS D"
          + " JOIN :dbRelationName R ON D.SYSTEM_ID = R.SYSTEM_ID"
          + " JOIN FFF_HITS_DETAILS H ON H.SYSTEM_ID = R.SYSTEM_ID"
          + " LEFT JOIN FOF_STATES S ON S.STATE_ID = D.TYPE"
          + " WHERE D.TYPE != 7 AND D.SYSTEM_ID IN (%s)"
          + " ORDER BY D.DECISION_DATE DESC";

  private final DecisionRowMapper decisionRowMapper;
  private final String dbRelationName;

  @Override
  public List<Decision> fetchDecisions(Connection connection, String id) throws SQLException {
    return fetchDecisions(connection, singletonList(id)).getOrDefault(id, emptyList());
  }

  @SuppressWarnings("findsecbugs:SQL_INJECTION_JDBC")
  Map<String, List<Decision>> fetchDecisions(Connection connection, List<String> ids)
      throws SQLException {
    if (log.isDebugEnabled())
      log.debug("Fetching decisions: systemIds={}, dbRelationName={}", ids, dbRelationName);

    Map<String, List<Decision>> result = new HashMap<>();
    String query = QueryStatementHelper.prepareQuery(DECISIONS_QUERY, dbRelationName, ids);

    if (log.isTraceEnabled())
      log.trace("Executing SQL: decisionsPerRecord={}, query={}", DECISIONS_PER_RECORD, query);

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setFetchSize(ids.size() * DECISIONS_PER_RECORD);
      QueryStatementHelper.setQueryParameters(statement, ids);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Decision decision = decisionRowMapper.mapRow(resultSet);
          addDecision(decision, resultSet.getString("system_id"), result);
        }
      }
    }

    return result;
  }

  private static void addDecision(
      Decision decision, String systemId, Map<String, List<Decision>> decisions) {
    if (log.isTraceEnabled())
      log.trace("Adding decision: decision={}, count={}", decision.id(), decisions.size());

    decisions.computeIfAbsent(systemId, s -> new ArrayList<>()).add(decision);
  }
}
