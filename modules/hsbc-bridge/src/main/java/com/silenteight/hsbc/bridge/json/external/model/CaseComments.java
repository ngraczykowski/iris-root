package com.silenteight.hsbc.bridge.json.external.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen", date = "2021-04-30T09:49:39.903Z[GMT]")
public class CaseComments {

  private String dnCASECOMMENTCommentId = null;
  private String dnCASECOMMENTCommentedBy = null;
  private String dnCASECOMMENTCommentDateTime = null;
  private String dnCASECOMMENTCaseComment = null;
  private String dnCASECOMMENTDeletedFlag = null;
  private String dnCASECOMMENTRestrictingPermission = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.commentId")
  public String getDnCASECOMMENTCommentId() {
    return dnCASECOMMENTCommentId;
  }

  public void setDnCASECOMMENTCommentId(String dnCASECOMMENTCommentId) {
    this.dnCASECOMMENTCommentId = dnCASECOMMENTCommentId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.commentedBy")
  public String getDnCASECOMMENTCommentedBy() {
    return dnCASECOMMENTCommentedBy;
  }

  public void setDnCASECOMMENTCommentedBy(String dnCASECOMMENTCommentedBy) {
    this.dnCASECOMMENTCommentedBy = dnCASECOMMENTCommentedBy;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.commentDateTime")
  public String getDnCASECOMMENTCommentDateTime() {
    return dnCASECOMMENTCommentDateTime;
  }

  public void setDnCASECOMMENTCommentDateTime(String dnCASECOMMENTCommentDateTime) {
    this.dnCASECOMMENTCommentDateTime = dnCASECOMMENTCommentDateTime;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.caseComment")
  public String getDnCASECOMMENTCaseComment() {
    return dnCASECOMMENTCaseComment;
  }

  public void setDnCASECOMMENTCaseComment(String dnCASECOMMENTCaseComment) {
    this.dnCASECOMMENTCaseComment = dnCASECOMMENTCaseComment;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.deletedFlag")
  public String getDnCASECOMMENTDeletedFlag() {
    return dnCASECOMMENTDeletedFlag;
  }

  public void setDnCASECOMMENTDeletedFlag(String dnCASECOMMENTDeletedFlag) {
    this.dnCASECOMMENTDeletedFlag = dnCASECOMMENTDeletedFlag;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASECOMMENT.restrictingPermission")
  public String getDnCASECOMMENTRestrictingPermission() {
    return dnCASECOMMENTRestrictingPermission;
  }

  public void setDnCASECOMMENTRestrictingPermission(String dnCASECOMMENTRestrictingPermission) {
    this.dnCASECOMMENTRestrictingPermission = dnCASECOMMENTRestrictingPermission;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaseComments caseComments = (CaseComments) o;
    return Objects.equals(dnCASECOMMENTCommentId, caseComments.dnCASECOMMENTCommentId) &&
        Objects.equals(dnCASECOMMENTCommentedBy, caseComments.dnCASECOMMENTCommentedBy) &&
        Objects.equals(dnCASECOMMENTCommentDateTime, caseComments.dnCASECOMMENTCommentDateTime) &&
        Objects.equals(dnCASECOMMENTCaseComment, caseComments.dnCASECOMMENTCaseComment) &&
        Objects.equals(dnCASECOMMENTDeletedFlag, caseComments.dnCASECOMMENTDeletedFlag) &&
        Objects.equals(
            dnCASECOMMENTRestrictingPermission, caseComments.dnCASECOMMENTRestrictingPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        dnCASECOMMENTCommentId, dnCASECOMMENTCommentedBy, dnCASECOMMENTCommentDateTime,
        dnCASECOMMENTCaseComment, dnCASECOMMENTDeletedFlag, dnCASECOMMENTRestrictingPermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseComments {\n");

    sb
        .append("    dnCASECOMMENTCommentId: ")
        .append(toIndentedString(dnCASECOMMENTCommentId))
        .append("\n");
    sb
        .append("    dnCASECOMMENTCommentedBy: ")
        .append(toIndentedString(dnCASECOMMENTCommentedBy))
        .append("\n");
    sb
        .append("    dnCASECOMMENTCommentDateTime: ")
        .append(toIndentedString(dnCASECOMMENTCommentDateTime))
        .append("\n");
    sb
        .append("    dnCASECOMMENTCaseComment: ")
        .append(toIndentedString(dnCASECOMMENTCaseComment))
        .append("\n");
    sb
        .append("    dnCASECOMMENTDeletedFlag: ")
        .append(toIndentedString(dnCASECOMMENTDeletedFlag))
        .append("\n");
    sb
        .append("    dnCASECOMMENTRestrictingPermission: ")
        .append(toIndentedString(dnCASECOMMENTRestrictingPermission))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
