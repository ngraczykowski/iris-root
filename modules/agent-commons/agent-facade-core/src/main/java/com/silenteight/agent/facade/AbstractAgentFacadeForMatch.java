package com.silenteight.agent.facade;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;

abstract class AbstractAgentFacadeForMatch<AgentInputT extends AgentInput>
    implements AgentFacade<AgentExchangeRequest, AgentExchangeResponse> {

  @Override
  public String getFacadeName() {
    return AgentFacade.super.getFacadeName();
  }

  protected abstract AgentOutput getAgentResponsesForMatch(AgentInputT agentInput);
}
