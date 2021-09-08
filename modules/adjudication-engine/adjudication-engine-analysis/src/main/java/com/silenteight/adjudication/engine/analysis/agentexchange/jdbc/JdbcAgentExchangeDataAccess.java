package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeDataAccess;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class JdbcAgentExchangeDataAccess implements AgentExchangeDataAccess {

  private final DeleteAgentExchangeMatchFeatureQuery deleteAgentExchangeMatchFeatureQuery;
  private final DeleteEmptyAgentExchange deleteEmptyAgentExchange;

  @Override
  public void removeAgentExchange() {
    deleteAgentExchangeMatchFeatureQuery.execute();
    deleteEmptyAgentExchange.execute();
  }
}
