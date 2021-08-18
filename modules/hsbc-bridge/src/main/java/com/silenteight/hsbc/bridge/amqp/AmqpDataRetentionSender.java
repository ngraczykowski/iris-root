package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.hsbc.bridge.retention.DataRetentionMessageSender;

import org.springframework.amqp.core.AmqpTemplate;

@Builder
@Slf4j
class AmqpDataRetentionSender implements DataRetentionMessageSender {

  private final AmqpTemplate amqpTemplate;
  private final Configuration configuration;

  @Override
  public void send(AlertsExpired alertsExpired) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        alertsExpired);
  }

  @Override
  public void send(PersonalInformationExpired personalInformationExpired) {
    amqpTemplate.convertAndSend(
        configuration.getExchangeName(),
        configuration.getRoutingKey(),
        personalInformationExpired);
  }

  @Builder
  @Value
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
