package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.TriggerAlertRequest;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_ADDED_TO_ANALYSIS;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INPUT_ACCEPTED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class TriggerAnalysisEndpoint {

  private final TriggerAlertAnalysisService triggerAlertAnalysisService;

  @ServiceActivator(inputChannel = ALERT_INPUT_ACCEPTED, outputChannel = ALERT_ADDED_TO_ANALYSIS)
  AlertAddedToAnalysisEvent apply(AlertInputAcceptedEvent event) {
    log.info("Adding alert [{}], ae name: [{}] to analysis",
        event.getAlertId(), event.getAlertRegisteredName());
    triggerAlertAnalysisService.triggerAlertAnalysis(
        TriggerAlertRequest.builder().alertNames(List.of(event.getAlertRegisteredName())).build());

    return new AlertAddedToAnalysisEvent(event.getAlertId(), event.getAlertRegisteredName());
  }
}
