package com.silenteight.sep.base.common.messaging;

import com.silenteight.sep.base.common.protocol.AnyUtils;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.support.converter.MessageConversionException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextBytes;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProtoMessageConverterTest {

  private static final String ANY_TYPE_NAME = Any.getDescriptor().getFullName();
  private static final long TIMESTAMP_SECONDS = 1212121212L;
  private static final int TIMESTAMP_NANOS = 123456;

  private MessageRegistry messageRegistry =
      new MessageRegistryFactory("com.google.protobuf").create();

  private ProtoMessageConverter underTest = new ProtoMessageConverter(messageRegistry);

  @Test
  void throwsOnNotAMessage() {
    assertThatThrownBy(() -> toMessage("invalid")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void throwsWhenContentTypeInvalid() {
    Message message = MessageBuilder
        .withBody(nextBytes(1))
        .setContentType("application/octet-stream")
        .build();

    assertThatThrownBy(() -> fromMessage(message))
        .isInstanceOf(MessageConversionException.class);
  }

  private static Empty emptyMessage() {
    return Empty.newBuilder().build();
  }

  private static Timestamp timestampMessage() {
    return Timestamp.newBuilder().setSeconds(TIMESTAMP_SECONDS).setNanos(TIMESTAMP_NANOS).build();
  }

  private com.google.protobuf.Message convert(Message message) {
    return (com.google.protobuf.Message) fromMessage(message);
  }

  private Object fromMessage(Message message) {
    return underTest.fromMessage(message);
  }

  private Message toMessage(Object object) {
    return toMessage(object, MessagePropertiesBuilder
        .newInstance()
        .setCorrelationId(randomAlphanumeric(16))
        .build());
  }

  private Message toMessage(Object object, MessageProperties properties) {
    return underTest.toMessage(object, properties);
  }


  @Nested
  class ToMessageTest {

    private static final String TEST_APP = "test app";

    private Empty dummyMessage;

    @BeforeEach
    void setUp() {
      dummyMessage = emptyMessage();
    }

    @Test
    void passesPropertiesToMessage() {
      MessageProperties properties = MessagePropertiesBuilder
          .newInstance()
          .setAppId(TEST_APP)
          .build();

      Message message = toMessage(emptyMessage(), properties);

      assertThat(message.getMessageProperties().getAppId()).isEqualTo(TEST_APP);
    }

    @Test
    void wrapsMessageWithAny() throws InvalidProtocolBufferException {
      underTest.setWrapWithAny(true);

      byte[] body = convertAndGetBody();

      assertThat(Any.parseFrom(body).unpack(Empty.class)).isEqualTo(dummyMessage);
    }

    @Test
    void doesNotWrapMessageWithAny() throws InvalidProtocolBufferException {
      underTest.setWrapWithAny(false);

      byte[] body = convertAndGetBody();

      assertThat(Empty.parseFrom(body)).isEqualTo(dummyMessage);
    }

    private byte[] convertAndGetBody() {
      return toMessage(dummyMessage).getBody();
    }
  }

  @Nested
  class FromMessageTest {

    private static final String UNKNOWN_TYPE = "unknown.Type";

    private Message timestampMessage;
    private Message anyMessage;

    @BeforeEach
    void setUp() {
      underTest.setWrapWithAny(false);
      timestampMessage = toMessage(timestampMessage());
      anyMessage = toMessage(AnyUtils.pack(emptyMessage()));
    }

    @Test
    void wrapsUnknownMessage() {
      underTest.setWrapUnknown(true);

      Message message = unknownMessage();
      Any anyMessage = (Any) convert(message);

      assertThat(anyMessage.getTypeUrl()).endsWith(UNKNOWN_TYPE);
      assertThat(anyMessage.getValue().toByteArray()).isEqualTo(message.getBody());
    }

    @Test
    void throwsWhenUnknownMessage() {
      underTest.setWrapUnknown(false);

      assertThatThrownBy(() -> convert(unknownMessage()))
          .isInstanceOf(MessageConversionException.class)
          .hasMessageStartingWith("Unknown message type");
    }

    private Message unknownMessage() {
      return MessageBuilder.fromClonedMessage(timestampMessage).setType(UNKNOWN_TYPE).build();
    }

    @Test
    @SuppressWarnings("squid:S2970")
    void treatsMessageWithoutTypeAsEmpty() {
      assertSoftly(softly -> {
        softly.assertThat(convert(messageWithoutType(anyMessage)))
              .isInstanceOfSatisfying(
                  Empty.class,
                  e -> assertThat(e.getUnknownFields().asMap()).containsKeys(1));

        softly.assertThat(convert(messageWithoutType(timestampMessage)))
              .isInstanceOfSatisfying(
                  Empty.class,
                  e -> assertThat(e.getUnknownFields().asMap()).containsKeys(1, 2));
      });
    }

    private Message messageWithoutType(Message prototypeMessage) {
      return MessageBuilder.fromClonedMessage(prototypeMessage).setType(null).build();
    }
  }
}
