package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class AlertMessageReceivedListener {

  private final QueueUpAlertMessageService service;

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void onReceived(FircoAlertMessage message) {
    service.queueUp(message);
  }

}
