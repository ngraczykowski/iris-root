package com.silenteight.warehouse.simulation.processing.mapping;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class PayloadConverter {

  private static final String DELIMITER = "_";

  private final ObjectMapper objectMapper;

  String convertPayload(Struct payload) {
    Map<String, String> mapPayload = flattenPayload(payload);
    return convertPayloadToString(mapPayload);
  }

  private String convertPayloadToString(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new SimulationPayloadException(e);
    }
  }

  @NotNull
  private static Map<String, String> flattenPayload(Struct payload) {
    return createFlattenMap(payload);
  }

  @NotNull
  private static Map<String, String> createFlattenMap(Struct payload) {
    return payload
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> flattenEntry(entry.getKey(), entry.getValue()))
        .flatMap(Collection::stream)
        .collect(toMap(Element::getKey, el -> el.getValue().getStringValue()));
  }

  private static List<Element> flattenEntry(String key, Value value) {
    if (value.hasStructValue())
      return flattenStruct(value.getStructValue(), key);

    return of(Element.of(key, value));
  }

  @NotNull
  private static List<Element> flattenStruct(Struct struct, String key) {
    return struct
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> Element.of(buildFlattenKey(key, entry), entry.getValue()))
        .collect(toList());
  }

  @NotNull
  private static String buildFlattenKey(String key, Entry<String, Value> entry) {
    return key + DELIMITER + entry.getKey();
  }

  @lombok.Value(staticConstructor = "of")
  private static class Element {

    String key;
    Value value;
  }
}
