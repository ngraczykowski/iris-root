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
  private final DeleteEmptyAgentExchange deleteEmptyAgentExchange;

  @Override
  public void removeAgentExchange(
      UUID agentExchangeRequestID, long matchID, List<String> featuresIDs) {
    deleteAgentExchangeMatchFeatureQuery.execute(agentExchangeRequestID, matchID, featuresIDs);
    deleteEmptyAgentExchange.execute();
  }
}
