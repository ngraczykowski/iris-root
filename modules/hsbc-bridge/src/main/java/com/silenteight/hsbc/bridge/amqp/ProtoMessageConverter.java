package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.google.protobuf.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.trimToNull;

@RequiredArgsConstructor
class ProtoMessageConverter extends AbstractMessageConverter {

  private static final String CONTENT_TYPE = "application/x-protobuf";

  private static final boolean DEFAULT_WRAP_UNKNOWN = true;

  private static final boolean DEFAULT_WRAP_WITH_ANY = true;

  private static final String ANY_TYPE_NAME = Any.getDescriptor().getFullName();

  private static final String EMPTY_TYPE_NAME = Empty.getDescriptor().getFullName();

  private final MessageRegistry messageRegistry;

  /**
   * When the target type is unknown to MessageRegistry, then payload is wrapped in Any message.
   */
  @Setter
  private boolean wrapUnknown = DEFAULT_WRAP_UNKNOWN;

  /**
   * Whether each created message should be wrapped with Any.
   */
  @Setter
  private boolean wrapWithAny = DEFAULT_WRAP_WITH_ANY;

  @Override
  protected Message createMessage(Object object, MessageProperties messageProperties) {
    validateObject(object);

    var message = maybeWrapWithAny(object);
    var descriptor = message.getDescriptorForType();
    var body = message.toByteArray();

    return MessageBuilder
        .withBody(body)
        .andProperties(messageProperties)
        .setContentType(CONTENT_TYPE)
        .setContentLength(body.length)
        .setType(descriptor.getFullName())
        .build();
  }

  @Override
  public Object fromMessage(Message message) throws MessageConversionException {
    var messageProperties = message.getMessageProperties();

    validateContentType(messageProperties.getContentType());

    try {
      return parseAndMaybeUnpack(
          getMessageTypeName(messageProperties.getType()),
          message.getBody());
    } catch (InvalidProtocolBufferException e) {
      throw new MessageConversionException("Failed to parse message.", e);
    }
  }

  private void validateContentType(String contentType) {
    if (!CONTENT_TYPE.equals(contentType)) {
      throw new MessageConversionException("Unsupported content type " + contentType);
    }
  }

  private com.google.protobuf.Message maybeWrapWithAny(Object object) {
    var message = (com.google.protobuf.Message) object;
    var typeName = message.getDescriptorForType().getFullName();

    if (!wrapWithAny || ANY_TYPE_NAME.equals(typeName)) {
      return message;
    }

    return Any.pack(message);
  }

  private void validateObject(Object object) {
    if (!(object instanceof com.google.protobuf.Message)) {
      throw new IllegalArgumentException("Invalid message");
    }
  }

  private Object parseAndMaybeUnpack(String typeName, byte[] body)
      throws InvalidProtocolBufferException {

    if (ANY_TYPE_NAME.equals(typeName)) {
      return Any.parseFrom(body);
    }

    return parseMessage(typeName, body)
        .orElseThrow(() -> new MessageConversionException("Unknown message type " + typeName));
  }

  private Optional<Object> parseMessage(String typeName, byte[] body)
      throws InvalidProtocolBufferException {
    var parser = messageRegistry.findParser(typeName);

    if (parser.isEmpty()) {
      if (!wrapUnknown)
        return empty();

      return of(wrap(typeName, ByteString.copyFrom(body)));
    }

    return of(parser.get().parseFrom(body));
  }

  private static String getMessageTypeName(String type) {
    return ofNullable(trimToNull(type)).orElse(EMPTY_TYPE_NAME);
  }

  private static Any wrap(String typeName, ByteString value) {
    return Any
        .newBuilder()
        .setTypeUrl(typeName)
        .setValue(value)
        .build();
  }
}
