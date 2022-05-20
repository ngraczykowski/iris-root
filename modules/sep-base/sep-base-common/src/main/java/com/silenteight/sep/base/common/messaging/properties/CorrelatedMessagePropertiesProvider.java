package com.silenteight.sep.base.common.messaging.properties;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.messaging.MessageHeaders;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.amqp.core.MessageProperties.DEFAULT_PRIORITY;
import static org.springframework.amqp.support.AmqpHeaders.CORRELATION_ID;
import static org.springframework.amqp.support.AmqpHeaders.EXPIRATION;
import static org.springframework.amqp.support.AmqpHeaders.MESSAGE_ID;
import static org.springframework.amqp.support.AmqpHeaders.REPLY_TO;

@Slf4j
@EqualsAndHashCode
@RequiredArgsConstructor
public final class CorrelatedMessagePropertiesProvider
    implements CustomizableMessagePropertiesProvider {

  static final String HEADER_PRIORITY = "priority";

  private static final String EMPTY_VALUE = "";

  private final MessageHeaders headers;

  @Override
  public MessageBuilderSupport<MessageProperties> customize(
      MessageBuilderSupport<MessageProperties> builder) {

    return new Correlator(builder).correlate();
  }

  @RequiredArgsConstructor
  private class Correlator {

    private final MessageBuilderSupport<MessageProperties> builder;

    MessageBuilderSupport<MessageProperties> correlate() {
      builder.setPriority(getHeaderOrDefault(HEADER_PRIORITY, DEFAULT_PRIORITY, Integer.class));
      setCorrelationIdOrMessageId();
      setReplyHeaders();
      return builder;
    }

    private <T> T getHeaderOrDefault(String key, T defaultValue, Class<T> clazz) {
      if (headers.containsKey(key)) {
        try {
          return headers.get(key, clazz);
        } catch (Exception e) {
          log.warn("Message header '{}' is unreadable", key, e);
        }
      }
      return defaultValue;
    }

    private void setCorrelationIdOrMessageId() {
      String correlationId = getHeaderOrDefault(CORRELATION_ID, EMPTY_VALUE, String.class);

      if (isNotEmpty(correlationId)) {
        builder.setCorrelationId(correlationId);
      } else {
        String messageId = getHeaderOrDefault(MESSAGE_ID, EMPTY_VALUE, String.class);
        if (isNotEmpty(messageId))
          builder.setCorrelationId(messageId);
      }
    }

    private void setReplyHeaders() {
      passThroughHeader(REPLY_TO);
      passThroughHeader(EXPIRATION);
    }

    private void passThroughHeader(String headerName) {
      if (headers.containsKey(headerName))
        builder.setReplyTo(headers.get(REPLY_TO, String.class));
    }
  }
}
