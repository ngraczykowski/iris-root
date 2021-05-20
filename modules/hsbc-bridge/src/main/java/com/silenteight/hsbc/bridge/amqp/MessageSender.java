package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.hsbc.bridge.report.WarehouseMessageSender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

@Builder
@Slf4j
class MessageSender implements WarehouseMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final MessageConverter messageConverter;
  private final Configuration configuration;

  @Override
  public void send(ProductionDataIndexRequest dataIndexRequest) {
    var amqpMessage = messageConverter.toMessage(dataIndexRequest, new MessageProperties());

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
