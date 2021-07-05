package com.silenteight.hsbc.bridge.json;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertConversionException;
import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter;
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class ObjectMapperJsonConverter implements ObjectConverter, AlertPayloadConverter {

  private final TypeReference<Map<String, String>> mapTypeReference = new TypeReference<>() {};
  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(Include.NON_NULL);

  @Override
  public <T> T convert(byte[] src, Class<T> valueType) throws ObjectConversionException {
    try {
      return objectMapper.readValue(src, valueType);
    } catch (RuntimeException | IOException e) {
      log.error("Error on payload conversion", e);
      throw new ObjectConversionException(e);
    }
  }

  @Override
  public byte[] convert(Object value) {
    try {
      return objectMapper.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      log.error("Error on object conversion", e);
      throw new JsonConversionException();
    }
  }

  @Override
  public AlertData convertAlertData(byte[] payload) throws AlertConversionException {
    try {
      return convert(payload, AlertData.class);
    } catch (Exception ex) {
      log.error("Alert data conversion error", ex);
      throw new AlertConversionException("Alert data conversion error", ex);
    }
  }

  @Override
  public Map<String, String> convertAlertDataToMap(AlertData alertData) throws AlertConversionException {
    var result = new HashMap<String, String>();

    var caseInformation = convertToMap(alertData.getCaseInformation());
    if (caseInformation != null) {
      result.putAll(caseInformation);
    }

    var alertMap =
        objectMapper.convertValue(alertData, new TypeReference<Map<String, Object>>() {});
    alertMap.remove("DN_CASE");

    alertMap.values().stream()
        .map(value -> flattenMap((List<?>) value))
        .forEach(result::putAll);

    return result;
  }

  @Override
  public void convertAndConsumeAlertData(
      InputCommand command, Consumer<AlertDataComposite> consumer) {
    try (var parser = getJsonFactory().createParser(command.getInputStream())) {

      if (parser.nextToken() != JsonToken.START_ARRAY) {
        throw new JsonConversionException("Missing array token at the start of the file");
      }

      while (parser.nextToken() == JsonToken.START_OBJECT) {
        tryToParseAndConsumeAlertData(command, consumer, parser);
      }
    } catch (RuntimeException | IOException exception) {
      log.error("Error on parsing json", exception);
      throw new JsonConversionException("Error on parsing the input stream", exception);
    }
  }

  private void tryToParseAndConsumeAlertData(
      InputCommand command, Consumer<AlertDataComposite> consumer,
      com.fasterxml.jackson.core.JsonParser parser) {
    try {
      var alertData = objectMapper.readValue(parser, AlertData.class);
      var payload = objectMapper.writeValueAsBytes(alertData);

      consumer.accept(new AlertDataComposite(command.getBulkId(), payload));
    } catch (Exception ex) {
      log.error("Error on parsing json object, cannot create Alert Data", ex);
    }
  }

  private Map<String, String> flattenMap(List<?> list) {
    var flattenedMap = new HashMap<String, String>();
    var maps = convertToMapList(list);

    var index = new AtomicInteger(1);
    StreamEx.of(maps)
        .zipWith(Stream.generate(index::getAndIncrement))
        .forKeyValue((map, idx) -> EntryStream.of(map)
            .mapKeys(key -> withPrefix(idx, key))
            .forKeyValue(flattenedMap::put));

    return flattenedMap;
  }

  private static String withPrefix(int index, String key) {
    return index + "." + key;
  }

  private List<Map<String, String>> convertToMapList(List<?> list) {
    return list.stream()
        .map(this::convertToMap)
        .collect(Collectors.toList());
  }

  private Map<String, String> convertToMap(Object object) throws AlertConversionException {
    try {
      return objectMapper.convertValue(object, mapTypeReference);
    } catch (IllegalArgumentException ex) {
      log.error("Alert data conversion error", ex);
      throw new AlertConversionException("Alert data conversion error", ex);
    }
  }

  private JsonFactory getJsonFactory() {
    return objectMapper.getFactory();
  }

}
