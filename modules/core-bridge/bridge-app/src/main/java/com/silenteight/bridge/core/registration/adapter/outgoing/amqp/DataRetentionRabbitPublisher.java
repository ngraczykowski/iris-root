package com.silenteight.bridge.core.registration.adapter.outgoing.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.AlertToRetention;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionAlertsExpiredEvent;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionPublisher;
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingDataRetentionProperties;
import com.silenteight.dataretention.api.v1.AlertData;
import com.silenteight.dataretention.api.v1.AlertsExpired;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class DataRetentionRabbitPublisher implements DataRetentionPublisher {

  private final AmqpRegistrationOutgoingDataRetentionProperties properties;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(DataRetentionAlertsExpiredEvent event) {
    log.info("Sending AlertsExpired with [{}] alerts", event.alerts().size());
    var message = AlertsExpired.newBuilder()
        .addAllAlerts(extractAlertNames(event.alerts()))
        .addAllAlertsData(mapToAlertsData(event.alerts()))
        .build();
    rabbitTemplate.convertAndSend(properties.exchangeName(), properties.alertsExpiredRoutingKey(),
        message);
  }

  private List<String> extractAlertNames(List<AlertToRetention> alerts) {
    return alerts.stream()
        .map(AlertToRetention::alertName)
        .filter(Objects::nonNull)
        .toList();
  }

  private List<AlertData> mapToAlertsData(List<AlertToRetention> alerts) {
    return alerts.stream()
        .map(alert -> AlertData.newBuilder()
            .setBatchId(alert.batchId())
            .setAlertId(alert.alertId())
            .setAlertName(Optional.ofNullable(alert.alertName()).orElse(""))
            .build())
        .toList();
  }
}
