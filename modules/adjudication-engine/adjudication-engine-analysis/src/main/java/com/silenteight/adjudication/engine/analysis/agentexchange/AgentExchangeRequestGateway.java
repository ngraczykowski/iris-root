package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

public interface AgentExchangeRequestGateway {

  void send(AgentExchangeRequest agentExchangeRequest);
}
