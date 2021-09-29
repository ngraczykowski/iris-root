package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.channel.AlertRegisteredChannel;
import com.silenteight.payments.bridge.event.AlertRegistered;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Qualifier(AlertRegisteredChannel.QUALIFIER)
@Slf4j
@RequiredArgsConstructor
class AlertRegisteredChannelInterceptor implements ChannelInterceptor {

  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;

  @Override
  public Message<?> preSend(Message<?> message, @Nonnull MessageChannel channel) {
    if (!(message.getPayload() instanceof AlertRegistered)) {
      log.warn("Enrichment skipped because the message payload if of the different type [{}] "
          + "that expected", AlertRegistered.class.getName());
      return message;
    }

    AlertRegistered alertRegistered = (AlertRegistered) message.getPayload();
    if (alertRegistered.getAlertModel().isEmpty()) {
      var model = alertMessageUseCase.findByAlertMessageId(alertRegistered.getAlertId());
      alertRegistered.withAlertModel(model);
    }
    if (alertRegistered.getOriginalMessage().isEmpty()) {
      var originalMessage = alertMessagePayloadUseCase
          .findByAlertMessageId(alertRegistered.getAlertId());
      alertRegistered.withOriginalMessage(originalMessage);
    }
    return message;
  }
}
