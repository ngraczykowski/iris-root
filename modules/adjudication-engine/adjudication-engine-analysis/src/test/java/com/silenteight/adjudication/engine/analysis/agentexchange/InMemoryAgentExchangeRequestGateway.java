package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;

import java.util.ArrayList;

class InMemoryAgentExchangeRequestGateway implements AgentExchangeRequestGateway {

  ArrayList<AgentExchangeRequestMessage> messages = new ArrayList<>();

  @Override
  public void send(
      AgentExchangeRequestMessage agentExchangeRequest) {
    messages.add(agentExchangeRequest);
  }

  public int count() {
    return messages.stream().mapToInt(AgentExchangeRequestMessage::getMatchCount).sum();
  }
}
