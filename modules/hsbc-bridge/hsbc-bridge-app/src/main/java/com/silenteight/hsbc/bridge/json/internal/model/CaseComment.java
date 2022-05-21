package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

@Data
public class CaseComment implements com.silenteight.hsbc.datasource.datamodel.CaseComment {

  private String commentId;
  private String commentedBy;
  private String commentDateTime;
  private String caseComment;
  private String deletedFlag;
  private String restrictingPermission;
}
