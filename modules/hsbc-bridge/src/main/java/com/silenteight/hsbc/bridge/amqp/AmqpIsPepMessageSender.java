package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.ispep.IsPepMessageSender;
import com.silenteight.learningstore.v1.api.exchange.IsPepLearningStoreExchangeRequest;

import org.springframework.amqp.core.AmqpTemplate;

@Builder
@Slf4j
class AmqpIsPepMessageSender implements IsPepMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(IsPepLearningStoreExchangeRequest isPepLearningStoreExchangeRequest) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        isPepLearningStoreExchangeRequest);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
