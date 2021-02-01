package com.silenteight.sep.base.common.messaging.postprocessing;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
class EncryptMessagePostProcessor implements MessagePostProcessor, Ordered {

  private final AmqpMessageEncypter encrypter;

  @Override
  public Message postProcessMessage(Message message) {
    return encrypter.encrypt(message);
  }

  @Override
  public int getOrder() {
    return MessageProcessorsOrdering.ENCRYPTION;
  }
}
