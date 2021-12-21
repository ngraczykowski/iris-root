package com.silenteight.warehouse.production.persistence.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Struct;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class PayloadConverter {

  @NonNull
  private final ObjectMapper objectMapper;

  public String convertPayload(Struct payload) {
    Map<String, String> mapPayload = convertPayloadToMap(payload);
    return convertPayloadToJson(mapPayload);
  }

  public Map<String, String> convertPayloadToMap(Struct struct) {
    return struct
        .getFieldsMap()
        .keySet()
        .stream()
        .collect(toMap(
            identity(),
            key -> struct.getFieldsMap().get(key).getStringValue()));
  }

  public String convertPayloadToJson(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new PayloadParsingException(e);
    }
  }
}
