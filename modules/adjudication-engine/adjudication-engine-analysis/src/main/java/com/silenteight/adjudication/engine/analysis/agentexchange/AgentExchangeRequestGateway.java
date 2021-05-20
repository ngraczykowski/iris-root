package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;

public interface AgentExchangeRequestGateway {

  void send(AgentExchangeRequestMessage agentExchangeRequest);
}
