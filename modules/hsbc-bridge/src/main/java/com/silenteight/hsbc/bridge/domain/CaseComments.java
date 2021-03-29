package com.silenteight.hsbc.bridge.domain;

import lombok.Data;

@Data
public class CaseComments {

  private Integer caseId;
  private Integer commentId;
  private Integer commentedBy;
  private String commentDateTime;
  private String caseComment;
  private Integer deletedFlag;
  private String restrictingPermission;
}
