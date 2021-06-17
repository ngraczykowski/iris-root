package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.model.transfer.WorldCheckMessageSender;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

@Builder
class ModelPersistedMessageSender implements WorldCheckMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(@NonNull ModelPersisted modelPersisted) {
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
