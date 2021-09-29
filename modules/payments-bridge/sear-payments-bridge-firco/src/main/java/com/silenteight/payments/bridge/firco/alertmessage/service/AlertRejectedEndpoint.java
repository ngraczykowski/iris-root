package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertStored;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REJECTED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;

@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class AlertRejectedEndpoint {

  private final ResponseGeneratorService responseGeneratorService;
  private final AlertMessageStatusService alertMessageStatusService;

  @ServiceActivator(inputChannel = ALERT_REJECTED)
  public void rejectAlertMessage(AlertStored alertStored) {
    var alertId = alertStored.getAlertModel().getId();
    responseGeneratorService.prepareAndSendResponse(alertId, REJECTED_OVERFLOWED);
    alertMessageStatusService.transitionAlertMessageStatus(
        alertId, REJECTED_OVERFLOWED);
  }

}
