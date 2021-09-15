package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class AlertMessageReceivedListener {

  private final AlertMessageStoredService service;

  @TransactionalEventListener
  public void onReceived(FircoAlertMessage alertMessage) {
    service.send(alertMessage);
  }

}
