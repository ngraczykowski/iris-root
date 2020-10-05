package com.silenteight.sep.base.common.messaging.encryption;

import org.springframework.amqp.core.Message;

import java.util.function.Function;

public interface AmqpMessageDecrypter extends Function<Message, Message> {

  /**
   * Decrypts AMQP message.
   *
   * @param message
   *     Message to be decrypted. Implementation should not modify input message.
   *
   * @return Decrypted version of input message.
   */
  Message decrypt(Message message);

  @Override
  default Message apply(Message message) {
    return decrypt(message);
  }
}
