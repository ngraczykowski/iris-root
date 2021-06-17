package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.hsbc.bridge.report.WarehouseMessageSender;

import org.springframework.amqp.core.AmqpTemplate;

@Builder
@Slf4j
class AmqpWarehouseMessageSender implements WarehouseMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(ProductionDataIndexRequest dataIndexRequest) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        dataIndexRequest);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
