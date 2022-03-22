package com.silenteight.agent.facade.exchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentReasonMapper {

  public static <T extends MessageOrBuilder> Struct mapToStruct(T reason) {
    var structBuilder = Struct.newBuilder();
    return addToStructBuilder(reason, structBuilder).build();
  }

  private static <T extends MessageOrBuilder> Struct.Builder addToStructBuilder(
      T reason, Struct.Builder builder) {
    try {
      JsonFormat
          .parser()
          .merge(JsonFormat.printer().print(reason), builder);
    } catch (InvalidProtocolBufferException e) {
      throw new MappingException(reason.getClass(), e);
    }
    return builder;
  }

  public static Struct mapToStruct(String fieldName, String value) {
    var structBuilder = Struct.newBuilder();

    structBuilder.putFields(
        fieldName,
        Value.newBuilder().setStringValue(value).build());

    return structBuilder.build();
  }

  private static class MappingException extends RuntimeException {

    private static final long serialVersionUID = 8725377083280882817L;

    MappingException(Class clazz, Throwable throwable) {
      super(format("Could not map %s into Struct", clazz.getName()), throwable);
    }
  }
}
