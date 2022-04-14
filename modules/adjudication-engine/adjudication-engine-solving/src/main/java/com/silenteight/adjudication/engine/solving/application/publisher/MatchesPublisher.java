package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchesPublisher {

  private final RabbitTemplate rabbitTemplate;

  public void publish(AgentExchangeRequest request) {
    log.info("AgentExchangeRequest supported features:");
  }
}
