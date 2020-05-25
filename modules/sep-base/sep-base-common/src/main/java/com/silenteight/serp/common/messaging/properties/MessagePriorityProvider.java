package com.silenteight.serp.common.messaging.properties;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

public class MessagePriorityProvider implements MessagePropertiesProvider {

  private final MessageProperties properties;

  public MessagePriorityProvider(int priority) {
    properties = MessagePropertiesBuilder
        .newInstance()
        .setPriority(priority)
        .build();
  }

  @Override
  public MessageProperties get() {
    return properties;
  }
}
