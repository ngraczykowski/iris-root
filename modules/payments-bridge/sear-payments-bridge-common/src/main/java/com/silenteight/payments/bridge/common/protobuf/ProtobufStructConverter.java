package com.silenteight.payments.bridge.common.protobuf;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import com.google.protobuf.util.JsonFormat.Printer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import javax.annotation.Nonnull;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProtobufStructConverter {

  private static final Parser JSON_PARSER = JsonFormat.parser().ignoringUnknownFields();
  protected static final Printer JSON_PRINTER = JsonFormat.printer();

  @NonNull
  private final ObjectMapper objectMapper;

  ProtobufStructConverter() {
    this(new ObjectMapper());

    objectMapper.findAndRegisterModules();
  }

  @Nonnull
  public Optional<Struct> toStruct(@NonNull Object anyObject) {
    return convertObjectToJson(anyObject).flatMap(this::convertJsonToStruct);
  }

  @Nonnull
  private Optional<String> convertObjectToJson(Object anyObject) {
    try {
      return of(objectMapper.writeValueAsString(anyObject));
    } catch (JsonProcessingException e) {
      log.warn("Failed to convert object to JSON", e);
      return empty();
    }
  }

  @Nonnull
  private Optional<Struct> convertJsonToStruct(String json) {
    var builder = Struct.newBuilder();

    try {
      JSON_PARSER.merge(json, builder);
    } catch (InvalidProtocolBufferException e) {
      log.warn("Failed to convert JSON to Struct", e);
      return empty();
    }

    return of(builder.build());
  }

  @Nonnull
  public <T> Optional<T> fromStruct(Class<T> type, Struct struct) {
    return convertStructToJson(struct).map(json -> convertJsonToObject(type, json));
  }

  @Nonnull
  private static Optional<String> convertStructToJson(Struct struct) {
    try {
      return of(JSON_PRINTER.print(struct));
    } catch (InvalidProtocolBufferException e) {
      log.warn("Failed to convert Struct to JSON", e);
      return empty();
    }
  }

  @Nonnull
  private <T> T convertJsonToObject(Class<T> type, String json) {
    return objectMapper.convertValue(json, type);
  }
}
