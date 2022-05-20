package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.recommendation.port.NotifyResponseCompletedPort;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class ResponseCompletedListener {

  private final NotifyResponseCompletedPort notifyResponseCompletedPort;

  @TransactionalEventListener
  public void onResponseCompleted(ResponseCompleted responseCompleted) {
    notifyResponseCompletedPort.notify(responseCompleted);
  }

}
