package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
public class Alerts {

  @JsonProperty("bulkId")
  private String bulkId;
  @JsonProperty("alerts")
  private List<AlertData> alerts;
}
