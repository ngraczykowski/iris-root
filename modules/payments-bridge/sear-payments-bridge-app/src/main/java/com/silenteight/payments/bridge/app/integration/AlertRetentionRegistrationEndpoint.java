package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.ZoneOffset;
import java.util.Set;

@MessageEndpoint
@RequiredArgsConstructor
class AlertRetentionRegistrationEndpoint {

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;

  @ServiceActivator(inputChannel = AlertAddedToAnalysisEvent.CHANNEL)
  void apply(AlertAddedToAnalysisEvent event) {
    var alertDto = alertMessagePayloadUseCase.findByAlertMessageId(event.getAlertId());
    var alertRetention = new AlertDataRetention(event.getAeAlert().getAlertName(),
        alertDto.getFilteredAt(ZoneOffset.UTC));
    createAlertRetentionUseCase.create(Set.of(alertRetention));
  }
}
