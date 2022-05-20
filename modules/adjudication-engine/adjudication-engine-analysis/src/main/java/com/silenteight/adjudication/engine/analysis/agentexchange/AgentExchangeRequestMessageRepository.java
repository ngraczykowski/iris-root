package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;

import java.util.List;

public interface AgentExchangeRequestMessageRepository {

  void save(AgentExchangeRequestMessage message);

  void saveAll(List<AgentExchangeRequestMessage> messages);
}
