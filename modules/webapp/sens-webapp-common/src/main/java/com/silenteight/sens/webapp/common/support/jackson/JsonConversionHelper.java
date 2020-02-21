package com.silenteight.sens.webapp.common.support.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public enum JsonConversionHelper {
  INSTANCE;

  private final ObjectMapper objectMapper;
  private final JavaType objectNodeType;
  private final JavaType arrayNodeType;

  JsonConversionHelper() {
    objectMapper = new ObjectMapper().registerModules(JacksonModuleFinder.findModules());
    objectNodeType = objectMapper.constructType(ObjectNode.class);
    arrayNodeType = objectMapper.constructType(ArrayNode.class);
  }

  public ObjectMapper objectMapper() {
    return objectMapper;
  }

  public ObjectNode serializeObject(Object object) {
    return objectMapper.convertValue(object, objectNodeType);
  }

  public <T> ArrayNode serializeArray(T[] array) {
    return objectMapper.convertValue(array, arrayNodeType);
  }

  public <T> ArrayNode serializeCollection(Collection<T> collection) {
    return objectMapper.convertValue(collection, arrayNodeType);
  }

  public <T> ArrayNode serializeStream(Stream<T> stream) {
    ArrayNode arrayNode = objectMapper.createArrayNode();
    stream.map(element -> objectMapper.convertValue(element, JsonNode.class))
        .forEach(arrayNode::add);
    return arrayNode;
  }

  public <K, V> ObjectNode serializeMap(Map<K, V> map) {
    return objectMapper.convertValue(map, objectNodeType);
  }

  public <T> T deserializeObject(ObjectNode node, Class<T> type) {
    return objectMapper.convertValue(node, type);
  }

  public <T> T deserializeObject(ObjectNode node, TypeReference<T> type) {
    return objectMapper.convertValue(node, type);
  }

  public <T> T[] deserializeArray(ArrayNode node, Class<T> elementType) {
    ArrayType type = objectMapper.getTypeFactory().constructArrayType(elementType);
    return objectMapper.convertValue(node, type);
  }

  public <T> List<T> deserializeCollection(ArrayNode node, Class<T> elementType) {
    CollectionType type = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, elementType);
    return objectMapper.convertValue(node, type);
  }

  public <T> List<T> deserializeCollection(String jsonString, Class<T> elementType) {
    objectMapper.getTypeFactory()
        .constructCollectionType(List.class, elementType);
    try {
      return objectMapper.readValue(jsonString, List.class);
    } catch (IOException e) {
      throw new FailedToParseJsonException(jsonString, e);
    }
  }

  public String serializeToString(TreeNode node) {
    try {
      return objectMapper.writeValueAsString(node);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Failed to serialize object to string.", e);
    }
  }

  public <T extends TreeNode> T deserializeFromString(String jsonString, JavaType type) {
    try {
      return objectMapper.readValue(jsonString, type);
    } catch (IOException e) {
      throw new FailedToParseJsonException(jsonString, e);
    }
  }

  public ObjectNode deserializeFromString(String jsonString) {
    try {
      return objectMapper.readValue(jsonString, objectNodeType);
    } catch (IOException e) {
      throw new FailedToParseJsonException(jsonString, e);
    }
  }

  public String prettyPrint(Object value) {
    try {
      return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(value);
    } catch (Exception e) {
      throw new FailedToGenerateJsonException(value, e);
    }
  }

  public <T> JavaType createType(Class<T> type) {
    return objectMapper.getTypeFactory().constructType(type);
  }

  public static final class FailedToGenerateJsonException extends RuntimeException {

    private static final long serialVersionUID = -5469532072591567562L;

    public FailedToGenerateJsonException(Object value, Throwable cause) {
      super("Failed to generate json while serializing object " + value, cause);
    }
  }


  public static final class FailedToParseJsonException extends IllegalArgumentException {

    private static final String MESSAGE = "Failed to parse JSON.";
    private static final long serialVersionUID = -3802013529730325381L;

    private final String json;

    public FailedToParseJsonException(String json) {
      super(MESSAGE);
      this.json = json;
    }

    public FailedToParseJsonException(String json, Throwable cause) {
      super(MESSAGE, cause);
      this.json = json;
    }

    public String getJson() {
      return json;
    }
  }
}
