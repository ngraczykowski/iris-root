package com.silenteight.agent.facade;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;

public interface AgentFacadeForMatch<V extends AgentInput>
    extends AgentFacade<AgentExchangeRequest, AgentExchangeResponse> {

  AgentOutput getAgentResponsesForMatch(V agentInput);
}
