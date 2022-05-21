package com.silenteight.hsbc.bridge.bulk.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlertReRecommend {

  @JsonProperty("alerts")
  List<AlertIdWrapper> alerts;

  public AlertReRecommend(List<AlertIdWrapper> alerts) {
    this.alerts = alerts;
  }
}
