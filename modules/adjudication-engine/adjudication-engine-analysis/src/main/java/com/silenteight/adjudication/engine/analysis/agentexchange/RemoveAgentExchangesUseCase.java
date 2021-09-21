package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AgentResponseStored;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "agentexchange" })
  void removeAgentExchanges(AgentResponseStored deleteAgentExchangeRequests) {
    var agentExchangeIds = new ArrayList<UUID>();
    var matchIds = new ArrayList<Long>();
    var features = new ArrayList<String>();
    deleteAgentExchangeRequests.getStoredMatchFeaturesList().forEach(
        request -> request.getFeaturesList().forEach(feature -> {
          agentExchangeIds.add(UUID.fromString(request.getRequest()));
          matchIds.add(request.getMatchId());
          features.add(feature);
        }));
    agentExchangeDataAccess.removeAgentExchange(agentExchangeIds, matchIds, features);
  }
}
