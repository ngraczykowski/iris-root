package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.core.annotation.Order;

@Order(PostProcessorsOrdering.ENCRYPTION)
@RequiredArgsConstructor
public class EncryptMessagePostProcessor implements MessagePostProcessor {

  private final AmqpMessageEncypter encrypter;

  @Override
  public Message postProcessMessage(Message message) {
    return encrypter.encrypt(message);
  }
}
