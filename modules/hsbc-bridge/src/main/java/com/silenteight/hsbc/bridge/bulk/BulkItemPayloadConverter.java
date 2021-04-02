package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.rest.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
class BulkItemPayloadConverter {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  byte[] map(@NonNull Alert alert) {
    try {
      return objectMapper.writeValueAsBytes(alert);
    } catch (JsonProcessingException e) {
      log.error("Cannot convert the alert to payload", e);
      throw new BulkPayloadConversionException(e);
    }
  }

  class BulkPayloadConversionException extends RuntimeException {

    BulkPayloadConversionException(Exception exception) {
      super(exception);
    }
  }
}
