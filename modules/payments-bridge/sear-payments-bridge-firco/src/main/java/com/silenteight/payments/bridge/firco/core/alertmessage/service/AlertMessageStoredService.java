package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.AlertMessageStoredPublisherPort;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.STORED;

@RequiredArgsConstructor
@Slf4j
@Component
public class AlertMessageStoredService  {

  private final AlertMessageQueueOverflowedService alertMessageQueueOverflowedService;
  private final AlertMessageStoredPublisherPort alertMessageStoredPublisherPort;
  private final AlertMessageStatusService alertMessageStatusService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void send(FircoAlertMessage alertMessage) {
    if (alertMessageQueueOverflowedService.resolve(alertMessage)) {
      return;
    }

    alertMessageStoredPublisherPort.publish(alertMessage);
    alertMessageStatusService
        .transitionAlertMessageStatus(alertMessage.getId(), STORED);
  }

}
