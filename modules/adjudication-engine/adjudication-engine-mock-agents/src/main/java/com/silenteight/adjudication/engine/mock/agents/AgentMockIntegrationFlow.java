package com.silenteight.adjudication.engine.mock.agents;

import lombok.RequiredArgsConstructor;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Profile("mockagents")
class AgentMockIntegrationFlow extends IntegrationFlowAdapter {

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from("agentExchangeRequestOutboundChannel")
        .handle(AgentExchangeRequest.class, (p, h) -> mockResponse(p))
        .channel("agentResponseInboundChannel");
  }

  private AgentExchangeResponse mockResponse(AgentExchangeRequest request) {
    return AgentExchangeResponse
        .newBuilder()
        .addAllAgentOutputs(request
            .getMatchesList()
            .stream()
            .map(m -> AgentOutput
                .newBuilder()
                .setMatch(m)
                .addAllFeatures(request
                    .getFeaturesList()
                    .stream()
                    .map(f -> Feature.newBuilder().setFeature(f).build())
                    .collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList()))
        .build();
  }
}
