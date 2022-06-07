package com.silenteight.adjudication.engine.common.protobuf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProtoMessageToObjectNodeConverter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final MessageRegistry messageRegistry;

  private static final MapType MAP_TYPE = JsonConversionHelper.INSTANCE
      .objectMapper()
      .getTypeFactory()
      .constructMapType(LinkedHashMap.class, String.class, Object.class);

  public Optional<ObjectNode> convert(Message message) {
    var json = convertToJsonString(message);
    if (json.isEmpty()) {
      return Optional.empty();
    }
    return convertToObjectNode(json.get());
  }

  public ObjectNode convert(Object object) {
    return OBJECT_MAPPER.convertValue(object, ObjectNode.class);
  }

  public Optional<String> convertToJsonString(Message message) {
    try {
      return Optional.of(messageRegistry.toJson(message));
    } catch (InvalidProtocolBufferException e) {
      log.warn("Could not generate Json", e);
      return Optional.empty();
    }
  }

  public Optional<Map<String, Object>> convertJsonToMap(String json) {
    try {
      return Optional.of(OBJECT_MAPPER.readValue(json, MAP_TYPE));
    } catch (JsonProcessingException e) {
      log.warn("Could not parse Json to map", e);
      return Optional.empty();
    }
  }

  public static Optional<ObjectNode> convertToObjectNode(String json) {
    try {
      return Optional.of(OBJECT_MAPPER.readTree(json).deepCopy());
    } catch (JsonProcessingException e) {
      log.warn("Could not create ObjectNode", e);
      return Optional.empty();
    }
  }
}
