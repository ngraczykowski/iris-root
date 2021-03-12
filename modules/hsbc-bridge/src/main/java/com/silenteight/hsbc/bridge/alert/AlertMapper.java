package com.silenteight.hsbc.bridge.alert;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
class AlertMapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  RawAlert map(Alert alert) {
    var payload = new byte[0];
    var caseWithAlert = alert.getSystemInformation().getCasesWithAlertURL();
    try {
      payload = objectMapper.writeValueAsBytes(alert);
    } catch (JsonProcessingException e) {
      // TODO
      log.error("Exception occured on saving alert payload", e);
    }
    return new RawAlert(caseWithAlert.get(0).getId(), payload);
  }

  Alert map(RawAlert rawAlert) {
    var alert = new Alert();
    try {
      alert = objectMapper
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(rawAlert.getPayload(), Alert.class);
    } catch (IOException e) {
      // TODO
      log.error("Exception occured on saving alert payload", e);
    }
    return alert;
  }
}
