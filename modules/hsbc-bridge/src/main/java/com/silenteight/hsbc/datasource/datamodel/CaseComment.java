package com.silenteight.hsbc.datasource.datamodel;

public interface CaseComment {
  String getCommentId();
  String getCommentedBy();
  String getCommentDateTime();
  String getCaseComment();
  String getDeletedFlag();
  String getRestrictingPermission();
}
