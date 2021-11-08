package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.protocol.AnyUtils;
import com.silenteight.hsbc.bridge.protocol.MessageRegistry;

import com.google.common.base.Preconditions;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.util.Optional;

@RequiredArgsConstructor
@Builder
class AmqpProtoMessageConverter extends AbstractMessageConverter {

  private static final String CONTENT_TYPE = "application/x-protobuf";

  private static final boolean DEFAULT_UNPACK_ANY = true;

  private static final boolean DEFAULT_WRAP_UNKNOWN = true;

  private static final boolean DEFAULT_WRAP_WITH_ANY = true;

  private static final String ANY_TYPE_NAME = Any.getDescriptor().getFullName();

  private static final String EMPTY_TYPE_NAME = Empty.getDescriptor().getFullName();

  @NonNull
  private final MessageRegistry messageRegistry;

  /**
   * Whether this converter should try to unpack Any messages to their original message type.
   */
  @Builder.Default
  private final boolean unpackAny = DEFAULT_UNPACK_ANY;

  /**
   * When the target type is unknown to MessageRegistry, then payload is wrapped in Any message.
   */
  @Builder.Default
  private final boolean wrapUnknown = DEFAULT_WRAP_UNKNOWN;

  /**
   * Whether each created message should be wrapped with Any.
   */
  @Builder.Default
  private final boolean wrapWithAny = DEFAULT_WRAP_WITH_ANY;

  @NotNull
  @Override
  protected Message createMessage(Object object, MessageProperties messageProperties) {
    var message = maybeWrapWithAny(object);

    var typeName = message.getDescriptorForType().getFullName();
    byte[] body = message.toByteArray();

    return MessageBuilder
        .withBody(body)
        .andProperties(messageProperties)
        .setContentType(CONTENT_TYPE)
        .setContentLength(body.length)
        .setType(typeName)
        .build();
  }

  private com.google.protobuf.Message maybeWrapWithAny(Object object) {
    Preconditions.checkArgument(object instanceof com.google.protobuf.Message);

    var message = (com.google.protobuf.Message) object;
    var typeName = message.getDescriptorForType().getFullName();

    if (!wrapWithAny || ANY_TYPE_NAME.equals(typeName))
      return message;

    return AnyUtils.pack(message);
  }

  @NotNull
  @Override
  public Object fromMessage(Message message) {
    var contentType = message.getMessageProperties().getContentType();
    if (contentType != null && !CONTENT_TYPE.equals(contentType))
      throw new MessageConversionException("Unsupported content type " + contentType);

    try {
      return parseAndMaybeUnpack(
          getMessageTypeNameFromProperties(message.getMessageProperties()),
          message.getBody());
    } catch (InvalidProtocolBufferException e) {
      throw new MessageConversionException("Failed to parse message.", e);
    }
  }

  @NotNull
  private static String getMessageTypeNameFromProperties(MessageProperties properties) {
    var messageTypeName = StringUtils.trimToNull(properties.getType());

    // NOTE(ahaczewski): When type was not specified with the message, treating such message
    //  as Empty lets parser fill all fields as unknown without failing.
    if (messageTypeName == null)
      return EMPTY_TYPE_NAME;

    return messageTypeName;
  }

  private Object parseAndMaybeUnpack(String typeName, byte[] body)
      throws InvalidProtocolBufferException {

    if (ANY_TYPE_NAME.equals(typeName)) {
      var any = Any.parseFrom(body);
      if (unpackAny) {
        // NOTE(ahaczewski): Trying to unpack message might fail because packed type was not known
        //  to message registry. In such a case failing safe to returning correctly parsed Any
        //  is the best option. Anyway, for when a message class is known to message registry,
        //  parsing errors will still be fatal / exceptional.
        var unpackedMessage = parseMessage(typeName, body);
        if (unpackedMessage.isPresent())
          return unpackedMessage.get();
      }

      return any;
    }

    // NOTE(ahaczewski): For each concrete type there is only one way to fail: exceptional.
    //  Except for when converter user wants such types wrapped as Any.
    return parseMessage(typeName, body)
        .orElseThrow(() -> new MessageConversionException("Unknown message type " + typeName));
  }

  @NotNull
  // NOTE(ahaczewski): False positive - there is a call to isEmpty() which Sonar does not recognize.
  @SuppressWarnings("squid:S3655")
  private Optional<Object> parseMessage(String typeName, byte[] body)
      throws InvalidProtocolBufferException {
    var parser = messageRegistry.findParser(typeName);

    if (parser.isEmpty()) {
      if (!wrapUnknown)
        return Optional.empty();

      return Optional.of(AnyUtils.wrap(typeName, ByteString.copyFrom(body)));
    }

    return Optional.of(parser.get().parseFrom(body));
  }
}
