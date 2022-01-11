package com.silenteight.warehouse.test.hsbcbridgeclient.datageneration;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonGetter;

@Value
@Builder
public class AlertDataSource {

  String alertId;
  String flagKey;
  String recommendationDate;
  String recommendationYear;
  String recommendationMonth;
  String recommendationDay;

  @JsonGetter
  String getDiscriminator() {
    return String.join("_", alertId, flagKey);
  }
}
