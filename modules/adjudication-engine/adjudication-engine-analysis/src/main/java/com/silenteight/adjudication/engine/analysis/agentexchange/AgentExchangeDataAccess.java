package com.silenteight.adjudication.engine.analysis.agentexchange;

import java.util.List;
import java.util.UUID;

public interface AgentExchangeDataAccess {

  void removeAgentExchange(UUID agentExchangeRequestId, long matchId, List<String> featuresIds);
}
