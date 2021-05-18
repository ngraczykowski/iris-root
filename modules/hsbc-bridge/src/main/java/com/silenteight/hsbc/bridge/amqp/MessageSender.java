package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.report.AlertSender;
import com.silenteight.hsbc.bridge.report.AlertWithRecommendationSender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
class MessageSender implements AlertSender, AlertWithRecommendationSender {

  private final AmqpTemplate amqpTemplate;
  private final String exchangeName;

  @Override
  public void sendAlerts(Collection<Alert> alerts) {
    alerts.stream()
        .map(this::toMessage)
        .forEach(this::sendMessage);

    log.debug("Alerts have been sent, size={}", alerts.size());
  }

  @Override
  public void sendAlertsWithRecommendations(Collection<AlertWithRecommendation> alerts) {
    alerts.stream()
        .map(this::toMessage)
        .forEach(this::sendMessage);

    log.debug("Alerts with recommendations have been sent, size={}", alerts.size());
  }

  private void sendMessage(Message message) {
    amqpTemplate.convertAndSend(exchangeName, null, message);
  }

  private Message toMessage(Alert alert) {
    return new Message(alert.getMetadata().getBytes(), new MessageProperties());
  }

  // FIXME use proto as soon as definition is ready
  private Message toMessage(AlertWithRecommendation alertWithRecommendation) {
    return new Message(alertWithRecommendation.toString().getBytes(), new MessageProperties());
  }
}
