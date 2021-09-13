package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeDataAccess;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JdbcAgentExchangeDataAccess implements AgentExchangeDataAccess {

  private final DeleteAgentExchangeMatchFeatureQuery deleteAgentExchangeMatchFeatureQuery;
  private final DeleteEmptyAgentExchangesQuery deleteEmptyAgentExchangesQuery;

  @Override
  public void removeAgentExchange(
      List<UUID> agentExchangeRequestId, List<Long> matchId, List<String> featuresIds) {

    deleteAgentExchangeMatchFeatureQuery.execute(agentExchangeRequestId, matchId, featuresIds);
    deleteEmptyAgentExchangesQuery.execute();
  }
}
