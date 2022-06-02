package com.silenteight.adjudication.engine.mock.agents;

import lombok.RequiredArgsConstructor;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

@RequiredArgsConstructor
@Component
@Profile("mockagents")
class RabbitAgentMockIntegrationFlow {

  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "ae.tmp-agent-request")
  public void mockResponse(AgentExchangeRequest request, Message message) {
    var response =
        AgentExchangeResponse.newBuilder()
            .addAllAgentOutputs(
                request.getMatchesList().stream()
                    .map(
                        m ->
                            AgentOutput.newBuilder()
                                .setMatch(m)
                                .addAllFeatures(
                                    request.getFeaturesList().stream()
                                        .map(AgentMockedFeatures::getRandomFeature)
                                        .collect(Collectors.toList()))
                                .build())
                    .collect(Collectors.toList()))
            .build();

    rabbitTemplate.convertAndSend(
        "agent.response",
        "ae.agent.response",
        response,
        m -> {
          m.getMessageProperties()
              .getHeaders()
              .put(CORRELATION_ID, message.getMessageProperties().getHeaders().get(CORRELATION_ID));
          return m;
        });
  }
}
