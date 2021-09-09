package com.silenteight.adjudication.engine.analysis.agentexchange;

import java.util.List;
import java.util.UUID;

public interface AgentExchangeDataAccess {

  void removeAgentExchange(
      List<UUID> agentExchangeRequestId, List<Long> matchId, List<String> featuresIds);
}
