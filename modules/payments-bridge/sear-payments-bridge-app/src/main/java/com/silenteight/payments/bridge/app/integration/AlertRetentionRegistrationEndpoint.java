package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.ZoneOffset;
import java.util.Set;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_ADDED_TO_ANALYSIS;

@MessageEndpoint
@RequiredArgsConstructor
class AlertRetentionRegistrationEndpoint {

  private final CreateAlertDataRetentionUseCase createAlertRetentionUseCase;

  @ServiceActivator(inputChannel = ALERT_ADDED_TO_ANALYSIS)
  void apply(AlertAddedToAnalysisEvent event) {
    var alertDto = event.getData(AlertMessageDto.class);
    var alertRetention = new AlertDataRetention(event.getAlertRegisteredName(),
        alertDto.getFilteredAt(ZoneOffset.UTC));
    createAlertRetentionUseCase.create(Set.of(alertRetention));
  }
}
