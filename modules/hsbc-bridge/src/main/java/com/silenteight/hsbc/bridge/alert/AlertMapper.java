package com.silenteight.hsbc.bridge.alert;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

import java.io.IOException;

@Slf4j
class AlertMapper {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  byte[] map(Alert alert) {
    try {
      return objectMapper.writeValueAsBytes(alert);
    } catch (JsonProcessingException e) {
      // TODO
      log.error("Exception occured on saving alert payload", e);
      throw new RuntimeException("FIXME!!");
    }
  }

  Alert map(byte[] alertPayload) {
    try {
      return objectMapper.readValue(alertPayload, Alert.class);
    } catch (IOException e) {
      // TODO
      log.error("Exception occured on saving alert payload", e);
      throw new RuntimeException("FIXME!!");
    }
  }

  AlertEntity map(AlertRawData alertRawData) throws JsonProcessingException {
    var caseId = alertRawData.getCasesWithAlertURL().getId();

    return new AlertEntity(
        caseId,
        objectMapper.writeValueAsBytes(alertRawData)
    );
  }
}
