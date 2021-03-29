package com.silenteight.hsbc.bridge.domain;

import lombok.Data;

@Data
public class CaseHistory {

  private Integer caseId;
  private Integer modifiedBy;
  private String modifiedDateTime;
  private String attribute;
  private String oldValue;
  private String newValue;
  private String transition;
}
