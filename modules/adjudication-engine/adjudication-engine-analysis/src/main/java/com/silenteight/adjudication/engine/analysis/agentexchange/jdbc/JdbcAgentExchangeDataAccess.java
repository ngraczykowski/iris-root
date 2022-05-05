package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeDataAccess;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JdbcAgentExchangeDataAccess implements AgentExchangeDataAccess {

  private final DeleteAgentExchangeMatchFeatureQuery deleteAgentExchangeMatchFeatureQuery;
  private final DeleteEmptyAgentExchangesQuery deleteEmptyAgentExchangesQuery;
  private final SelectAgentExchangeMatchFeatureIdsByAlertIdsQuery
      selectAgentExchangeMatchFeatureIdsByAlertIdsQuery;

  private final DeleteAgentExchangeMatchFeatureByIdsQuery deleteAgentExchangeMatchFeatureByIdsQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void removeAgentExchange(
      List<UUID> agentExchangeRequestId, List<Long> matchId, List<String> featuresIds) {

    deleteAgentExchangeMatchFeatureQuery.execute(agentExchangeRequestId, matchId, featuresIds);
    deleteEmptyAgentExchangesQuery.execute();
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void removeAgentExchangeMatchFeatureByAlertIds(List<Long> ids) {
    deleteAgentExchangeMatchFeatureByIdsQuery.execute(ids);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<Long> selectAgentExchangeMatchFeatureIdsByAlertIds(List<Long> alertIds) {
    return selectAgentExchangeMatchFeatureIdsByAlertIdsQuery.execute(alertIds);
  }

}
