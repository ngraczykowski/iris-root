package com.silenteight.serp.common.messaging.properties;

import lombok.NonNull;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import java.time.Duration;

public class MessageIdAndPriorityProvider implements MessagePropertiesProvider {

  private final MessageProperties properties;

  public MessageIdAndPriorityProvider(
      @NonNull String messageId, int priority, Duration expiration) {

    properties = MessagePropertiesBuilder
        .newInstance()
        .setPriority(priority)
        .setMessageId(messageId)
        .setExpiration(Long.toString(expiration.toMillis()))
        .build();
  }

  public MessageIdAndPriorityProvider(@NonNull String messageId, int priority) {

    properties = MessagePropertiesBuilder
        .newInstance()
        .setPriority(priority)
        .setMessageId(messageId)
        .build();
  }

  @Override
  public MessageProperties get() {
    return properties;
  }
}
