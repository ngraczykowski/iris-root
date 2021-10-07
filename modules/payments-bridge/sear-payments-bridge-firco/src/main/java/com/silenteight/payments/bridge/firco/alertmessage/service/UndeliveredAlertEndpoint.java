package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_UNDELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class UndeliveredAlertEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;

  @ServiceActivator(inputChannel = ALERT_UNDELIVERED)
  void apply(AlertUndeliveredEvent event) {
    alertMessageStatusService.transitionAlertMessageStatus(
        event.getAlertId(), AlertMessageStatus.valueOf(event.getStatus()), UNDELIVERED);
  }
}
