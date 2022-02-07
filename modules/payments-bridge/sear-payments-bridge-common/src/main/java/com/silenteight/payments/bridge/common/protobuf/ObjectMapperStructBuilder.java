package com.silenteight.payments.bridge.common.protobuf;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;

@Slf4j
public class ObjectMapperStructBuilder {

  private static final Parser JSON_TO_STRUCT_PARSER = JsonFormat.parser();

  private final ObjectMapper objectMapper;
  private final Builder payloadBuilder;

  public ObjectMapperStructBuilder(@NonNull ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.payloadBuilder = Struct.newBuilder();
  }

  public ObjectMapperStructBuilder(@NonNull ObjectMapper objectMapper, @NonNull Struct prototype) {
    this.objectMapper = objectMapper;
    this.payloadBuilder = Struct.newBuilder(prototype);
  }

  public Struct build() {
    return payloadBuilder.build();
  }

  public ObjectMapperStructBuilder add(Object payload) {
    try {
      var payloadJson = convertToJson(payload);
      mergePayload(payloadJson, payload.getClass());
    } catch (StructFromJsonBuilderException e) {
      log.warn("Cannot add payload", e);
    }

    return this;
  }

  private String convertToJson(Object payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new StructFromJsonBuilderException(
          "Unable to create JSON from payload", payload.getClass(), e);
    }
  }

  private void mergePayload(String payloadJson, Class<?> payloadType) {
    try {
      JSON_TO_STRUCT_PARSER.merge(payloadJson, payloadBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new StructFromJsonBuilderException(
          "Unable to merge payload JSON to Struct", payloadType, e);
    }
  }

  private static final class StructFromJsonBuilderException extends RuntimeException {

    private static final long serialVersionUID = 5098710193274122873L;

    @Getter
    private final Class<?> payloadType;

    StructFromJsonBuilderException(String message, Class<?> payloadType, Throwable cause) {
      super(message + ": " + payloadType.getName(), cause);

      this.payloadType = payloadType;
    }
  }
}
