package com.silenteight.adjudication.engine.analysis.agentresponse;

import lombok.RequiredArgsConstructor;

import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgentResponseFacade {

  private final ReceiveAgentExchangeResponseUseCase receiveAgentExchangeResponseUseCase;

  public void receiveAgentExchangeResponse(
      UUID agentExchangeRequestId, AgentExchangeResponse response) {

    receiveAgentExchangeResponseUseCase.receiveAgentExchangeResponse(
        agentExchangeRequestId, response);
  }
}
