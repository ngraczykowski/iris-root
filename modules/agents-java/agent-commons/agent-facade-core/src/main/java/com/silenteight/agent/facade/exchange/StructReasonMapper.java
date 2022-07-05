package com.silenteight.agent.facade.exchange;

import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.protobuf.Struct.newBuilder;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class StructReasonMapper {

  private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

  public static Struct mapToStruct(String fieldName, String value) {
    var structBuilder = Struct.newBuilder();

    structBuilder.putFields(
        fieldName,
        Value.newBuilder().setStringValue(value).build());

    return structBuilder.build();
  }

  static <T extends Structable> Struct toStruct(T reason) {
    try {
      var jsonValue = OBJECT_MAPPER.writeValueAsString(reason);
      var structBuilder = newBuilder();
      JsonFormat.parser().merge(jsonValue, structBuilder);
      return structBuilder.build();
    } catch (Exception exception) {
      throw new MappingException(reason.getClass().getName(), exception);
    }
  }

  private static class MappingException extends RuntimeException {

    private static final long serialVersionUID = 5365029238882413105L;

    MappingException(String className, Throwable throwable) {
      super(format("Could not map %s into Struct", className), throwable);
    }
  }

  private static ObjectMapper buildObjectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(NON_NULL);
    return objectMapper;
  }
}
