package com.silenteight.sep.base.common.messaging.encryption;

import org.springframework.amqp.core.Message;

import java.util.function.Function;

public interface AmqpMessageEncypter extends Function<Message, Message> {

  /**
   * Encrypts AMQP message.
   *
   * @param message
   *     Message to be encrypted. Implementation should not modify input message.
   *
   * @return Encrypted version of input message.
   */
  Message encrypt(Message message);

  @Override
  default Message apply(Message message) {
    return encrypt(message);
  }
}
