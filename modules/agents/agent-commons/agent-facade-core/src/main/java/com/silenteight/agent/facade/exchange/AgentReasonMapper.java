package com.silenteight.agent.facade.exchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;

import static java.lang.String.format;

/**
 * @deprecated We should not map protos to struct anymore, because we are trying to get rid of proto
 *     api dependencies in facade. Replaced by {@link StructReasonMapper}
 */
@Deprecated(forRemoval = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentReasonMapper {


  /**
   * @deprecated see {@link StructReasonMapper#toStruct(Structable)}
   */
  @Deprecated(forRemoval = true)
  public static <T extends MessageOrBuilder> Struct mapToStruct(T reason) {
    var structBuilder = Struct.newBuilder();
    return addToStructBuilder(reason, structBuilder).build();
  }

  private static <T extends MessageOrBuilder> Struct.Builder addToStructBuilder(
      T reason, Struct.Builder builder) {
    try {
      JsonFormat
          .parser()
          .merge(JsonFormat.printer().includingDefaultValueFields().print(reason), builder);
    } catch (InvalidProtocolBufferException e) {
      throw new MappingException(reason.getClass(), e);
    }
    return builder;
  }

  /**
   * @deprecated Moved to {@link StructReasonMapper#mapToStruct(String, String)}
   */
  @Deprecated(forRemoval = true)
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
