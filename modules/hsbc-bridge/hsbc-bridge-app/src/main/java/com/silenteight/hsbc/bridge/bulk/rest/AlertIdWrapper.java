package com.silenteight.hsbc.bridge.bulk.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor
public class AlertIdWrapper {

  @JsonProperty("alert_id")
  String alertId;

  public AlertIdWrapper(String alertId) {
    this.alertId = alertId;
  }
}
