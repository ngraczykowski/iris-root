package com.silenteight.adjudication.engine.solve;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

public interface AgentRequestGateway {

  void sendRequest(AgentExchangeRequest agentExchangeRequest);
}
