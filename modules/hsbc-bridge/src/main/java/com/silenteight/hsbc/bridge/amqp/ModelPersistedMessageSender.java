package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.bridge.model.transfer.WorldCheckMessageSender;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

@Builder
class ModelPersistedMessageSender implements WorldCheckMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final MessageConverter messageConverter;
  private final Configuration configuration;

  @Override
  public void send(@NonNull ModelPersisted modelPersisted) {
    var amqpMessage = messageConverter.toMessage(modelPersisted, new MessageProperties());

    sendMessage(amqpMessage);
  }

  private void sendMessage(Message message) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        message);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
