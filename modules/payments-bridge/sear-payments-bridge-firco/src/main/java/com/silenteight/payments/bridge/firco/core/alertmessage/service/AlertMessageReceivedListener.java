package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.AlertMessageModel;
import com.silenteight.payments.bridge.event.AlertStored;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
class AlertMessageReceivedListener {

  private final CommonChannels commonChannels;

  @TransactionalEventListener
  public void onReceived(AlertMessageModel alertMessage) {
    commonChannels.alertStored().send(
        MessageBuilder.withPayload(
          new AlertStored(alertMessage)).build());
  }

}
