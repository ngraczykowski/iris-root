package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.model.transfer.HistoricalDecisionsMessageSender;
import com.silenteight.proto.historicaldecisions.model.v1.api.ModelPersisted;

import org.springframework.amqp.core.AmqpTemplate;

@Builder
class HistoricalDecisionsModelPersistedMessageSender implements HistoricalDecisionsMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(ModelPersisted modelPersisted) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        modelPersisted);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
