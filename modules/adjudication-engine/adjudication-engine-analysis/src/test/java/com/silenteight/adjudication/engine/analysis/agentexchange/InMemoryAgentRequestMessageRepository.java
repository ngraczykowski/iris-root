package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;

import java.util.ArrayList;
import java.util.List;

class InMemoryAgentRequestMessageRepository
    implements AgentExchangeRequestMessageRepository {

  private final ArrayList<AgentExchangeRequestMessage> store = new ArrayList<>();

  @Override
  public void save(
      AgentExchangeRequestMessage message) {
    store.add(message);
  }

  @Override
  public void saveAll(
      List<AgentExchangeRequestMessage> messages) {
    store.addAll(messages);
  }

  public int getMatchesCount() {
    return store.stream().mapToInt(AgentExchangeRequestMessage::getMatchCount).sum();
  }

  public void clear() {
    store.clear();
  }
}
