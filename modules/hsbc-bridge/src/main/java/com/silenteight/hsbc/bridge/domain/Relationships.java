package com.silenteight.hsbc.bridge.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Relationships {

  private Integer caseId;
  private BigDecimal inputId;
  private String inputName;
  private BigDecimal recordId;
  private BigDecimal relatedInputId;
  private String relatedInputName;
  private BigDecimal relatedRecordId;
  private String ruleName;
  private BigDecimal priorityScore;
  private String relationshipHash;
}
