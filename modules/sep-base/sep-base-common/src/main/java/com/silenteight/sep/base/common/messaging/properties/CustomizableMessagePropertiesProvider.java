package com.silenteight.sep.base.common.messaging.properties;

import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

@FunctionalInterface
public interface CustomizableMessagePropertiesProvider extends MessagePropertiesProvider {

  MessageBuilderSupport<MessageProperties> customize(
      MessageBuilderSupport<MessageProperties> builder);

  @Override
  default MessageProperties get() {
    return customize(MessagePropertiesBuilder.newInstance()).build();
  }
}
