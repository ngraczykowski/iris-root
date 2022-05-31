package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.agent.HistoricalDecisionMessageSender;
import com.silenteight.proto.learningstore.historicaldecision.v2.api.HistoricalDecisionLearningStoreExchangeRequest;

import org.springframework.amqp.core.AmqpTemplate;

@Builder
@Slf4j
class AmqpHistoricalDecisionMessageSender implements HistoricalDecisionMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(
      HistoricalDecisionLearningStoreExchangeRequest historicalDecisionLearningStoreExchangeRequest) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        historicalDecisionLearningStoreExchangeRequest);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
