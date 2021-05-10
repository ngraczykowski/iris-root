package com.silenteight.hsbc.bridge.json;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.Alerts;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

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
  public <T> T convert(Object fromValue, Class<T> toValueType) {
    return objectMapper.convertValue(fromValue, toValueType);
  }

  @Override
  public List<AlertData> convert(byte[] payload) {
    var alerts = convert(payload, Alerts.class);
    return alerts.getAlerts();
  }

  class JsonConversionException extends RuntimeException {

    private static final long serialVersionUID = 2587038986777201805L;
  }
}
