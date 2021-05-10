package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Relationship {

  private BigDecimal inputId;
  private String inputName;
  private String recordId;
  private BigDecimal relatedInputId;
  private String relatedInputName;
  private String relatedRecordId;
  private String ruleName;
  private BigDecimal priorityScore;
  private String relationshipHash;
}
