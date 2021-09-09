package com.silenteight.adjudication.engine.analysis.agentresponse;

import com.silenteight.adjudication.internal.v1.AgentResponseStored;

public interface DeleteAgentExchangeGateway {

  void send(AgentResponseStored deleteAgentExchange);
}
