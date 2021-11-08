package com.silenteight.hsbc.bridge.protocol;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Preconditions;
import com.google.protobuf.*;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.TypeRegistry;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
public class MessageRegistry {

  private final Map<String, MessageType> messageTypes = new TreeMap<>();
  private final TypeRegistry typeRegistry;

  MessageRegistry(Iterable<Class<? extends Message>> types) {
    types.forEach(this::tryToRegisterMessageType);
    typeRegistry = createTypeRegistry();
  }

  private void tryToRegisterMessageType(Class<? extends Message> type) {
    try {
      registerMessageType(type);
    } catch (Exception e) {
      log.warn("Failed to register message type, ignoring: class={}", type, e);
    }
  }

  @SuppressWarnings("unchecked")
  private void registerMessageType(Class<? extends Message> type) {

    Descriptor descriptor = (Descriptor) ReflectionUtils.invokeStaticGetter(type, "getDescriptor").orElse(null);
    Parser<Message> parser = (Parser<Message>) ReflectionUtils.invokeStaticGetter(type, "parser").orElse(null);

    if (descriptor == null || parser == null) {
      if (log.isDebugEnabled())
        log.debug("Ignoring type that is not a Protocol Buffers message: class={}", type);
      return;
    }

    String fullName = descriptor.getFullName();

    if (messageTypes.containsKey(fullName)) {
      Class<? extends Message> otherType = messageTypes.get(fullName).getType();
      log.warn("Duplicated message type, ignoring: class={}, otherClass={}", type, otherType);
      return;
    }

    MessageType messageType = MessageType.builder()
        .typeName(fullName)
        .type(type)
        .parser(parser)
        .descriptor(descriptor)
        .build();

    messageTypes.put(fullName, messageType);
  }

  private TypeRegistry createTypeRegistry() {
    TypeRegistry.Builder typeRegistryBuilder = TypeRegistry.newBuilder();

    messageTypes
        .values()
        .stream()
        .filter(Predicate.not(MessageType::isGoogleType))
        .map(MessageType::getDescriptor)
        .forEach(typeRegistryBuilder::add);

    return typeRegistryBuilder.build();
  }

  public Message unpackAny(Any any) {
    String typeUrl = any.getTypeUrl();
    return maybeUnpackAny(any).orElseThrow(
        () -> new MessageConversionException("Could not unpack message of type " + typeUrl));
  }

  @SuppressWarnings("unchecked")
  public Optional<Message> maybeUnpackAny(Any any) {
    String typeName = getTypeNameFromTypeUrl(any.getTypeUrl());

    Preconditions.checkArgument(
        !typeName.isBlank(), "Any message has invalid type URL %s", any.getTypeUrl());

    return findMessageType(typeName)
        .flatMap(messageType -> AnyUtils.maybeUnpack(any, (Class<Message>) messageType.getType()));
  }

  private Optional<MessageType> findMessageType(String typeName) {
    if (!messageTypes.containsKey(typeName))
      return Optional.empty();

    return Optional.of(messageTypes.get(typeName));
  }

  private static String getTypeNameFromTypeUrl(String typeUrl) {
    int pos = typeUrl.lastIndexOf('/');
    return pos == -1 ? "" : typeUrl.substring(pos + 1);
  }

  public Optional<Parser<Message>> findParser(String typeNameOrUrl) {
    Preconditions.checkArgument(!typeNameOrUrl.isBlank(), "Must provide type name or URL.");

    return findMessageType(getTypeName(typeNameOrUrl))
        .map(MessageType::getParser);
  }

  private static String getTypeName(String typeNameOrUrl) {
    if (typeNameOrUrl.contains("/"))
      return getTypeNameFromTypeUrl(typeNameOrUrl);

    return typeNameOrUrl;
  }

  public Stream<Class<? extends Message>> streamMessageTypes() {
    return messageTypes.values().stream().map(MessageType::getType);
  }

  public Optional<String> maybeToJson(Message message) {
    try {
      return Optional.of(toJson(message));
    } catch (InvalidProtocolBufferException e) {
      log.error("Cannot convert message to JSON: message={}", TextFormat.shortDebugString(message), e);
      return Optional.empty();
    }
  }

  public String toJson(Message message) throws InvalidProtocolBufferException {
    return JsonFormat
        .printer()
        .usingTypeRegistry(typeRegistry)
        .sortingMapKeys()
        .print(message);
  }

  @RequiredArgsConstructor
  @Getter
  @Builder
  private static final class MessageType {

    private final String typeName;
    private final Class<? extends Message> type;
    private final Parser<Message> parser;
    private final Descriptor descriptor;

    boolean isGoogleType() {
      return typeName.startsWith("google");
    }
  }
}
