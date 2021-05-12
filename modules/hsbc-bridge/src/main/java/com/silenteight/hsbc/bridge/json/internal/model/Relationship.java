package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

@Data
public class Relationship {

  private String inputId;
  private String inputName;
  private String recordId;
  private String relatedInputId;
  private String relatedInputName;
  private String relatedRecordId;
  private String ruleName;
  private String priorityScore;
  private String relationshipHash;
}
