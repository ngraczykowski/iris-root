package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class AlertRawMapper {

  private final ObjectMapper objectMapper;

  AlertRawMapper() {
    objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  AlertRawData map(@NonNull Alert alert) {
    var casesWithAlertURL = alert.getSystemInformation().getCaseWithAlertURL();

    return new AlertRawData(objectMapper.convertValue(casesWithAlertURL, CasesWithAlertURL.class));
  }

}
