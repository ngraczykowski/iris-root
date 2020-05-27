package com.silenteight.serp.common.messaging.properties;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageBuilderSupport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.serp.common.messaging.properties.CorrelatedMessagePropertiesProvider.HEADER_PRIORITY;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.amqp.support.AmqpHeaders.CORRELATION_ID;
import static org.springframework.amqp.support.AmqpHeaders.MESSAGE_ID;

class CorrelatedMessagePropertiesProviderTest {

  private CorrelatedMessagePropertiesProvider underTest;
  private MessageBuilderSupport<MessageProperties> builder;

  @BeforeEach
  void setUp() {
    MessageProperties properties = new MessageProperties();
    builder = MessagePropertiesBuilder.fromProperties(properties);
  }

  @Test
  void givenPriority_priorityIsSet() {
    // given
    Map<String, Object> headersMap = new HashMap<>();
    headersMap.put(HEADER_PRIORITY, 10);
    underTest = new CorrelatedMessagePropertiesProvider(new MessageHeaders(headersMap));
    // when
    MessageProperties resultProperties = underTest.customize(builder).build();
    // then
    assertThat(resultProperties.getPriority()).isEqualTo(10);
  }

  @Test
  void givenMessageIdAndCorrelationId_CorrelationIdIsSet() {
    // given
    Map<String, Object> headers = new HashedMap<>();
    headers.put(CORRELATION_ID, "12345");
    headers.put(MESSAGE_ID, "67890");
    underTest = new CorrelatedMessagePropertiesProvider(new MessageHeaders(headers));
    // when
    MessageProperties resultProperties = underTest.customize(builder).build();
    // then
    assertThat(resultProperties.getCorrelationId()).isEqualTo("12345");
    assertThat(resultProperties.getMessageId()).isNull();
  }

  @Test
  void givenMessageIdWithoutCorrelationId_CorrelationIdIsSet() {
    // given
    Map<String, Object> headers = new HashedMap<>();
    headers.put(MESSAGE_ID, "12345");
    underTest = new CorrelatedMessagePropertiesProvider(new MessageHeaders(headers));
    // when
    MessageProperties resultProperties = underTest.customize(builder).build();
    // then
    assertThat(resultProperties.getCorrelationId()).isEqualTo("12345");
  }

  @Test
  void givenNothing_defaultPriorityIsSet() {
    // given
    Map<String, Object> headers = new HashedMap<>();
    underTest = new CorrelatedMessagePropertiesProvider(new MessageHeaders(headers));
    // when
    MessageProperties resultProperties = underTest.customize(builder).build();
    // then
    assertThat(resultProperties.getPriority()).isEqualTo(0);
  }
}
