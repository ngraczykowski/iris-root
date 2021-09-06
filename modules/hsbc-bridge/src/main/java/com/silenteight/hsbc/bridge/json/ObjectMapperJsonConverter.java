package com.silenteight.hsbc.bridge.json;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertConversionException;
import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter;
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;
import com.silenteight.hsbc.bridge.bulk.exception.BatchAlertsLimitException;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
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
@RequiredArgsConstructor
class ObjectMapperJsonConverter implements ObjectConverter, AlertPayloadConverter {

  private final TypeReference<Map<String, String>> mapTypeReference = new TypeReference<>() {};
  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
      .setSerializationInclusion(Include.NON_NULL);
  private final int alertLimit;

  @Override
  public <T> T convert(byte[] payload, Class<T> valueType) throws ObjectConversionException {
    try {
      if (payload == null)
        throw new IllegalArgumentException("Payload given to conversion is null.");
      return objectMapper.readValue(payload, valueType);
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
      throw new JsonConversionException("Error on object conversion", e);
    }
  }

  @Override
  public AlertData convertAlertData(byte[] payload) throws AlertConversionException {
    try {
      return convert(payload, AlertData.class);
    } catch (Exception e) {
      log.error("Alert data conversion error", e);
      throw new AlertConversionException("Alert data conversion error", e);
    }
  }

  @Override
  public Map<String, String> convertAlertDataToMap(AlertData alertData) throws
      AlertConversionException {
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
      InputCommand command, Consumer<AlertDataComposite> consumer) throws IOException {
    try (var parser = getJsonFactory().createParser(command.getInputStream())) {

      if (parser.nextToken() != JsonToken.START_ARRAY) {
        throw new JsonConversionException("Missing array token at the start of the file");
      }
      var alertCount = 1;
      while (parser.nextToken() == JsonToken.START_OBJECT) {
        if (alertCount > alertLimit)
          throw new BatchAlertsLimitException(alertLimit);
        parseAndConsumeAlertData(command, consumer, parser);
        alertCount++;
      }
    } catch (BatchAlertsLimitException e) {
      throw e;
    } catch (RuntimeException exception) {
      log.error("Error on parsing json", exception);
      throw new JsonConversionException("Error on parsing the input stream", exception);
    }
  }

  private void parseAndConsumeAlertData(
      InputCommand command, Consumer<AlertDataComposite> consumer,
      com.fasterxml.jackson.core.JsonParser parser) throws IOException {
    var alertData = objectMapper.readValue(parser, AlertData.class);
    var payload = objectMapper.writeValueAsBytes(alertData);

    consumer.accept(new AlertDataComposite(command.getBulkId(), payload));
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
    } catch (IllegalArgumentException e) {
      log.error("Alert data conversion error", e);
      throw new AlertConversionException("Alert data conversion error", e);
    }
  }

  private JsonFactory getJsonFactory() {
    return objectMapper.getFactory();
  }

}
