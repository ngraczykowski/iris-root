package com.silenteight.sep.base.common.protocol;

import lombok.NonNull;

import com.google.protobuf.Any;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.HashSet;
import java.util.Set;

public class AnyProtoMessageConverter implements ConditionalGenericConverter {

  private static final TypeDescriptor ANY_TYPE = TypeDescriptor.valueOf(Any.class);
  private static final TypeDescriptor MESSAGE_TYPE = TypeDescriptor.valueOf(Message.class);

  private final MessageRegistry messageRegistry;

  private final Set<ConvertiblePair> convertiblePairs;

  public AnyProtoMessageConverter(@NonNull MessageRegistry messageRegistry) {
    this.messageRegistry = messageRegistry;

    convertiblePairs = createConvertiblePairs();
  }

  private Set<ConvertiblePair> createConvertiblePairs() {
    Set<ConvertiblePair> pairs = new HashSet<>();

    pairs.add(new ConvertiblePair(GeneratedMessageV3.class, Any.class));
    pairs.add(new ConvertiblePair(GeneratedMessage.class, Any.class));

    messageRegistry.streamMessageTypes().forEach(
        type -> pairs.add(new ConvertiblePair(Any.class, type)));

    return pairs;
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return convertiblePairs;
  }

  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (sourceType.isAssignableTo(ANY_TYPE)) {
      if (targetType.isAssignableTo(ANY_TYPE))
        return source;

      return convertFromAny((Any) source, targetType);
    }

    if (targetType.isAssignableTo(ANY_TYPE) && sourceType.isAssignableTo(MESSAGE_TYPE))
      return convertToAny((Message) source);

    throw new IllegalStateException("Unexpected source/target types");
  }

  private Object convertToAny(Message source) {
    return AnyUtils.pack(source);
  }

  private Object convertFromAny(Any source, TypeDescriptor targetType) {
    Message message = messageRegistry.unpackAny(source);

    TypeDescriptor messageTypeDescriptor = TypeDescriptor.valueOf(message.getClass());
    if (!messageTypeDescriptor.isAssignableTo(targetType)) {
      throw new IllegalStateException(
          "Unexpectedly converted to unexpected type " + messageTypeDescriptor);
    }

    return message;
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    if (sourceType.isAssignableTo(ANY_TYPE) && targetType.isAssignableTo(ANY_TYPE))
      return false;

    if (sourceType.isAssignableTo(ANY_TYPE))
      return true;

    if (targetType.isAssignableTo(ANY_TYPE))
      return sourceType.isAssignableTo(MESSAGE_TYPE);

    return false;
  }
}
