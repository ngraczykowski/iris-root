package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class AlertMessageReceivedListener {

  private final QueueUpAlertMessageService service;

  @TransactionalEventListener
  public void onReceived(FircoAlertMessage message) {
    service.queueUp(message);
  }

}
