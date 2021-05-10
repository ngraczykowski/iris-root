package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
public class Relationship {

  @JsonProperty("Relationships.Input Id")
  private BigDecimal inputId;
  @JsonProperty("Relationships.Input Name")
  private String inputName;
  @JsonProperty("Relationships.Record Id")
  private String recordId;
  @JsonProperty("Relationships.Related Input Id")
  private BigDecimal relatedInputId;
  @JsonProperty("Relationships.Related Input Name")
  private String relatedInputName;
  @JsonProperty("Relationships.Related Record Id")
  private String relatedRecordId;
  @JsonProperty("Relationships.Rule Name")
  private String ruleName;
  @JsonProperty("Relationships.Priority Score")
  private BigDecimal priorityScore;
  @JsonProperty("Relationships.Relationship Hash")
  private String relationshipHash;
}
