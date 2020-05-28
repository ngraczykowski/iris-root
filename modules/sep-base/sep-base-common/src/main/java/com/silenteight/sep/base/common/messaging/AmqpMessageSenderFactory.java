package com.silenteight.sep.base.common.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Collection;

@RequiredArgsConstructor
class AmqpMessageSenderFactory implements MessageSenderFactory {

  @NonNull
  private final AmqpTemplate template;
  @NonNull
  private final MessageConverter messageConverter;
  @NonNull
  private final Collection<SendMessageListener> listeners;

  @Override
  public MessageSender get(String exchangeName) {
    return AmqpMessageSender
        .builder()
        .exchange(exchangeName)
        .template(template)
        .messageConverter(messageConverter)
        .listeners(listeners)
        .build();
  }
}
