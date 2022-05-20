package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CaseHistory {

  @JsonProperty("DN_CASEHISTORY.modifiedBy")
  private String modifiedBy;
  @JsonProperty("DN_CASEHISTORY.modifiedDateTime")
  private String modifiedDateTime;
  @JsonProperty("DN_CASEHISTORY.attribute")
  private String attribute;
  @JsonProperty("DN_CASEHISTORY.oldValue")
  private String oldValue;
  @JsonProperty("DN_CASEHISTORY.newValue")
  private String newValue;
  @JsonProperty("DN_CASEHISTORY.transition")
  private String transition;
}
