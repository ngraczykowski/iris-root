package com.silenteight.warehouse.production.persistence.common;

import lombok.NonNull;
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
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class PayloadConverter {

  private static final String DELIMITER = "_";

  @NonNull
  private final ObjectMapper objectMapper;

  public String convertPayload(Struct payload) {
    Map<String, String> mapPayload = convertPayloadToMap(payload);
    return convertPayloadToJson(mapPayload);
  }

  public Map<String, String> convertPayloadToMap(Struct struct) {
    Struct flatMap = flattenPayload(struct);
    return flatMap
        .getFieldsMap()
        .keySet()
        .stream()
        .collect(toMap(
            identity(),
            key -> flatMap.getFieldsMap().get(key).getStringValue()));
  }

  public String convertPayloadToJson(Map<String, String> payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new PayloadParsingException(e);
    }
  }

  @NotNull
  private static Struct flattenPayload(Struct payload) {
    return Struct.newBuilder().putAllFields(createFlattenMap(payload)).build();
  }

  @NotNull
  private static Map<String, Value> createFlattenMap(Struct payload) {
    return payload
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> flattenEntry(entry.getKey(), entry.getValue()))
        .flatMap(Collection::stream)
        .collect(toMap(Element::getKey, Element::getValue));
  }

  private static List<Element> flattenEntry(String key, Value value) {
    if (value.hasStructValue())
      return flattenStruct(value.getStructValue(), key);

    return of(Element.of(key, value));
  }

  @NotNull
  private static List<Element> flattenStruct(Struct structValue, String key1) {
    return structValue
        .getFieldsMap()
        .entrySet()
        .stream()
        .map(entry -> Element.of(buildFlattenKey(key1, entry), entry.getValue()))
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
