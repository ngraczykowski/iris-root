package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CaseComment {

  @JsonProperty("DN_CASECOMMENT.commentId")
  private String commentId;
  @JsonProperty("DN_CASECOMMENT.commentedBy")
  private String commentedBy;
  @JsonProperty("DN_CASECOMMENT.commentDateTime")
  private String commentDateTime;
  @JsonProperty("DN_CASECOMMENT.caseComment")
  private String caseComment;
  @JsonProperty("DN_CASECOMMENT.deletedFlag")
  private String deletedFlag;
  @JsonProperty("DN_CASECOMMENT.restrictingPermission")
  private String restrictingPermission;
}
