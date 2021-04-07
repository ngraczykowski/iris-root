package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.rest.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
class AlertRawMapper {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  AlertRawData mapBulkPayload(@NonNull byte[] payload) {
    try {
      var alert = objectMapper.readValue(payload, Alert.class);
      return objectMapper.convertValue(alert.getSystemInformation(), AlertRawData.class);
    } catch (IOException e) {
      log.error("Exception occurred on converting bulk payload", e);
      throw new AlertRawDataConversionException(e);
    }
  }

  AlertRawData mapAlertPayload(@NonNull byte[] payload) {
    try {
      return objectMapper.readValue(payload, AlertRawData.class);
    } catch (IOException e) {
      log.error("Exception occurred on converting alert payload", e);
      throw new AlertRawDataConversionException(e);
    }
  }

  byte[] map(AlertRawData alertRawData) {
    try {
      return objectMapper.writeValueAsBytes(alertRawData);
    } catch (JsonProcessingException e) {
      var casesWithAlertUrl = alertRawData.getFirstCaseWithAlertURL();
      throw new AlertPreProcessingFailedException(casesWithAlertUrl.getKeyLabel());
    }
  }

  class AlertRawDataConversionException extends RuntimeException {

    private static final long serialVersionUID = 3812710263621549701L;

    AlertRawDataConversionException(Exception exception) {
      super(exception);
    }
  }
}
