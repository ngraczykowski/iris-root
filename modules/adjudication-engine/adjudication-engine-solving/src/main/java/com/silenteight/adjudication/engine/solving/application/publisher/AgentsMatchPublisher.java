package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.AgentsMatchPort;
import com.silenteight.adjudication.engine.solving.domain.AgentExchangeRequestMessage;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
class AgentsMatchPublisher implements AgentsMatchPort {

  public static final String AGENT_CONFIG_HEADER = "agentConfig";
  static final String AGENT_REQUEST_EXCHANGE_NAME = "agent.request";
  private final RabbitTemplate rabbitTemplate;

  private static String makeRoutingKey(String routingKey) {
    return routingKey.replace('.', '_').replace('/', '.');
  }

  public void publish(
      AgentExchangeRequestMessage message) {
    // TODO map and send to agents - remember about routing key and priority.
    // TODO make windowing
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
}
