package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.DeleteAgentExchangeRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Transactional
  void remove(List<DeleteAgentExchangeRequest> deleteAgentExchangeRequests) {
    var agentExchangeIds = new ArrayList<UUID>();
    var matchIds = new ArrayList<Long>();
    var features = new ArrayList<String>();
    deleteAgentExchangeRequests.forEach(
        request -> request.getFeaturesIds().forEach(feature -> {
          agentExchangeIds.add(request.getAgentExchangeRequestId());
          matchIds.add(request.getMatchId());
          features.add(feature);
        }));
    agentExchangeDataAccess.removeAgentExchange(agentExchangeIds, matchIds, features);
  }
}
