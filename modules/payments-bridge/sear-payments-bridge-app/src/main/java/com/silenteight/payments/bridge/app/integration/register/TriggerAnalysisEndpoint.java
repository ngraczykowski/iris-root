package com.silenteight.payments.bridge.app.integration.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.TriggerAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.TriggerAlertAnalysisUseCase;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class TriggerAnalysisEndpoint {

  private final TriggerAlertAnalysisUseCase triggerAlertAnalysisUseCase;

  @ServiceActivator(inputChannel = AlertInputAcceptedEvent.CHANNEL,
      outputChannel = AlertAddedToAnalysisEvent.CHANNEL)
  AlertAddedToAnalysisEvent apply(AlertInputAcceptedEvent event) {
    var aeAlert = event.getAeAlert();
    log.info("Adding alert [{}], ae name: [{}] to analysis",
        aeAlert.getAlertId(), aeAlert.getAlertName());

    var request = TriggerAlertRequest.builder()
        .alertNames(List.of(aeAlert.getAlertName()))
        .build();
    triggerAlertAnalysisUseCase.triggerAlertAnalysis(request);
    return new AlertAddedToAnalysisEvent(aeAlert);
  }
}
