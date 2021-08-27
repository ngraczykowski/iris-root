package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.DeleteAgentExchangeRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Transactional
  void remove(List<DeleteAgentExchangeRequest> deleteAgentExchangeRequests) {
    deleteAgentExchangeRequests.forEach(
        request -> agentExchangeDataAccess.removeAgentExchange(request.getAgentExchangeRequestId(),
            request.getMatchId(),
            request.getFeaturesIds()));
  }
}
