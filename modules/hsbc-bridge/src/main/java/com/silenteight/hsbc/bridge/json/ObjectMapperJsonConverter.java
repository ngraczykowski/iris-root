package com.silenteight.hsbc.bridge.json;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertConversionException;
import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter;
import com.silenteight.hsbc.bridge.alert.dto.AlertDataComposite;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.Alerts;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
class ObjectMapperJsonConverter implements ObjectConverter, AlertPayloadConverter {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(Include.NON_NULL);

  @Override
  public <T> T convert(byte[] src, Class<T> valueType) {
    try {
      return objectMapper.readValue(src, valueType);
    } catch (IOException e) {
      log.error("Error on payload conversion", e);
      throw new JsonConversionException();
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
  public List<AlertData> convert(byte[] payload) {
    var alerts = convert(payload, Alerts.class);
    return alerts.getAlerts();
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
  public void convertAndConsumeAlertData(
      InputCommand command, Consumer<AlertDataComposite> consumer) {
    try (var parser = getJsonFactory().createParser(command.getInputStream())) {

      if (parser.nextToken() != JsonToken.START_ARRAY) {
        throw new JsonConversionException("Missing array token at the start of the file");
      }

      while (parser.nextToken() == JsonToken.START_OBJECT) {
        tryToParseAndConsumeAlertData(command, consumer, parser);
      }
    } catch (IOException exception) {
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

  private JsonFactory getJsonFactory() {
    return objectMapper.getFactory();
  }

  @NoArgsConstructor
  class JsonConversionException extends RuntimeException {

    private static final long serialVersionUID = 2587038986777201805L;

    JsonConversionException(String message) {
      super(message);
    }

    JsonConversionException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }
}
