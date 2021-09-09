package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AgentResponseStored;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Transactional
  void remove(AgentResponseStored deleteAgentExchangeRequests) {
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
