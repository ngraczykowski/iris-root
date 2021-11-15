package com.silenteight.payments.bridge.app.integration.retention;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.integration.ChannelFactory;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.stream.Collectors;

@MessageEndpoint
@RequiredArgsConstructor
class CreateAlertRetentionEndpoint {

  static final String CREATE_ALERT_RETENTION_CHANNEL =
      "createAlertRetentionChannel";

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;

  @ServiceActivator(inputChannel = CREATE_ALERT_RETENTION_CHANNEL)
  void apply(List<LearningAlert> learningAlerts) {
    var alerts = learningAlerts.stream()
        .map(la -> new AlertDataRetention(la.getAlertName(), la.getAlertTime()))
        .collect(Collectors.toList());
    createAlertRetentionUseCase.create(alerts);
  }

  @Bean(CREATE_ALERT_RETENTION_CHANNEL)
  MessageChannel messageChannel() {
    return ChannelFactory.createDirectChannel();
  }

}
