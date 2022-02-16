package com.silenteight.warehouse.test.hsbcbridgeclient.datageneration;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonGetter;

@Value
@Builder
public class AlertDataSource {

  String alertId;
  String flagKey;
  String alertDate;
  String caseId;
  String currentState;

  @JsonGetter
  String getDiscriminator() {
    return String.join("_", alertId, flagKey);
  }
}
