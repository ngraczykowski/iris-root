package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.integration.channel.AlertDeliveredChannel;
import com.silenteight.payments.bridge.event.AlertDelivered;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Qualifier(AlertDeliveredChannel.QUALIFIER)
@Slf4j
@RequiredArgsConstructor
class AlertDeliveredChannelInterceptor implements ChannelInterceptor {

  private final AlertMessageUseCase alertMessageUseCase;

  @Override
  public Message<?> preSend(Message<?> message, @Nonnull MessageChannel channel) {
    if (!(message.getPayload() instanceof AlertDelivered)) {
      log.warn("Enrichment skipped because the message payload if of the different type [{}] "
          + "that expected", AlertDelivered.class.getName());
      return message;
    }

    AlertDelivered alertDelivered = (AlertDelivered) message.getPayload();
    if (alertDelivered.getAlertModel().isEmpty()) {
      var model = alertMessageUseCase
          .findByAlertMessageId(alertDelivered.getAlertId());
      alertDelivered.withAlertModel(model);
    }
    return message;
  }

}
