package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AgentExchangeRequestMessage;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgentsMatchPublisher {

  static final String AGENT_REQUEST_EXCHANGE_NAME = "agent.request";
  public static final String AGENT_CONFIG_HEADER = "agentConfig";
  private final RabbitTemplate rabbitTemplate;

  public void publish(AgentExchangeRequestMessage message) {
    // TODO map and send to agents - remember about routing key and priority.
    log.info("Sending agent requests for {}", message.getRequestId());
    var routing = makeRoutingKey(message.getAgentConfig());

    rabbitTemplate.convertAndSend(
        AGENT_REQUEST_EXCHANGE_NAME, routing, message.getAgentExchangeRequest(), request -> {
          //add headers
          request.getMessageProperties().setPriority(message.getPriority());
          request.getMessageProperties().setCorrelationId(message.getRequestId().toString());
          request.getMessageProperties().setHeader(AGENT_CONFIG_HEADER, message.getAgentConfig());
          return request;
        });

    //    this.auditLOg

    if (log.isDebugEnabled()) {
      log.debug(
          "Sent agent requests for {} matches to following agent: {}", message.getMatchesCount(),
          message.getAgentConfig());
    }
  }

  private static String makeRoutingKey(String routingKey) {
    return routingKey.replace('.', '_').replace('/', '.');
  }
}
