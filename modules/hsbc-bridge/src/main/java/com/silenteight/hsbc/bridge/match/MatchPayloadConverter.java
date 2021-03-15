package com.silenteight.hsbc.bridge.match;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
class MatchPayloadConverter {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  byte[] convert(@NonNull MatchRawData matchRawData) {
    try {
      return objectMapper.writeValueAsBytes(matchRawData);
    } catch (JsonProcessingException e) {
      log.error("Match data conversion error, {}", e);
      throw new PayloadConversionException("Cannot convert class to payload", e);
    }
  }

  MatchRawData convert(byte[] payload) {
    try {
      return objectMapper.readValue(payload, MatchRawData.class);
    } catch (IOException e) {
      log.error("Match data conversion error, {}", e);
      throw new PayloadConversionException("Cannot convert payload to class", e);
    }
  }

  class PayloadConversionException extends RuntimeException {

    public PayloadConversionException(String message, Exception exception) {
      super(message, exception);
    }
  }
}
