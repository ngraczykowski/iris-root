package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.report.WarehouseClient;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class MessageSender implements WarehouseClient {

  private final AmqpTemplate amqpTemplate;
  private final String exchangeName;

  @Override
  public void sendAlerts(Collection<Alert> alerts) {
    alerts.forEach(this::sendMessage);

    log.debug("Alerts have been send into exchange={}, size={}", exchangeName, alerts.size());
  }

  private void sendMessage(Alert alert) {
    amqpTemplate.convertAndSend(exchangeName, null, toMessage(alert));
  }

  private Message toMessage(Alert alert) {
    return new Message(alert.getMetadata().getBytes(), new MessageProperties());
  }
}
