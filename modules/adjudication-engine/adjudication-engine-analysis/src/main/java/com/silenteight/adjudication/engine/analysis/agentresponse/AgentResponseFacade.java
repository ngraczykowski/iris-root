package com.silenteight.adjudication.engine.analysis.agentresponse;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgentResponseFacade {

  private final ReceiveAgentExchangeResponseUseCase receiveAgentExchangeResponseUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Optional<MatchFeaturesUpdated> receiveAgentExchangeResponse(
      UUID agentExchangeRequestId, AgentExchangeResponse response) {

    return receiveAgentExchangeResponseUseCase.receiveAgentExchangeResponse(
        agentExchangeRequestId, response);
  }
}
