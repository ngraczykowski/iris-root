package com.silenteight.adjudication.engine.analysis.agentresponse;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgentResponseFacade {

  private final ReceiveAgentExchangeResponseUseCase receiveAgentExchangeResponseUseCase;

  public Optional<MatchFeaturesUpdated> receiveAgentExchangeResponse(
      UUID agentExchangeRequestId, AgentExchangeResponse response) {

    return receiveAgentExchangeResponseUseCase.receiveAgentExchangeResponse(
        agentExchangeRequestId, response);
  }
}
