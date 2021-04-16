package com.silenteight.adjudication.engine.solve.agentexchange;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

public interface AgentRequestGateway {

  void sendRequest(AgentExchangeRequest agentExchangeRequest);
}
