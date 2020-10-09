package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageDecrypter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.core.annotation.Order;

@Order(PostProcessorsOrdering.ENCRYPTION)
@RequiredArgsConstructor
public class DecryptMessagePostProcessor implements MessagePostProcessor {

  private final AmqpMessageDecrypter decrypter;

  @Override
  public Message postProcessMessage(Message message) {
    return decrypter.decrypt(message);
  }
}
