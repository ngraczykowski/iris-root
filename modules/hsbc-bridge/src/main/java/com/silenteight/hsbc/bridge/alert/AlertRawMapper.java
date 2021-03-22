package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
public
class AlertRawMapper {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  AlertRawData map(@NonNull Alert alert) {
    var casesWithAlertURL = alert.getSystemInformation().getCaseWithAlertURL();

    return new AlertRawData(objectMapper.convertValue(casesWithAlertURL, CasesWithAlertURL.class));
  }

  AlertRawData map(@NonNull byte[] payload) {
    try {
      return new AlertRawData(objectMapper.readValue(payload, CasesWithAlertURL.class));
    } catch (IOException e) {
      log.error("Exception occured on saving alert payload", e);
      throw new RuntimeException("FIXME!!");
    }
  }

}
