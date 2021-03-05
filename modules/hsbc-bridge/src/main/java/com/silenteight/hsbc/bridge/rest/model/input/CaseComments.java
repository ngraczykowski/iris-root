package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CaseComments {

  private Integer caseId = null;
  private Integer commentId = null;
  private Integer commentedBy = null;
  private String commentDateTime = null;
  private String caseComment = null;
  private Integer deletedFlag = null;
  private String restrictingPermission = null;

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
    this.caseId = caseId;
  }

  /**
   * Unique identifier assigned to a comment
   **/

  @Schema(description = "Unique identifier assigned to a comment")
  @JsonProperty("commentId")
  public Integer getCommentId() {
    return commentId;
  }

  public void setCommentId(Integer commentId) {
    this.commentId = commentId;
  }

  /**
   * Which User was the author of the comment
   **/

  @Schema(description = "Which User was the author of the comment")
  @JsonProperty("commentedBy")
  public Integer getCommentedBy() {
    return commentedBy;
  }

  public void setCommentedBy(Integer commentedBy) {
    this.commentedBy = commentedBy;
  }

  /**
   * Date and time the comment was made
   **/

  @Schema(description = "Date and time the comment was made")
  @JsonProperty("commentDateTime")
  public String getCommentDateTime() {
    return commentDateTime;
  }

  public void setCommentDateTime(String commentDateTime) {
    this.commentDateTime = commentDateTime;
  }

  /**
   * Displays the comment
   **/

  @Schema(description = "Displays the comment")
  @JsonProperty("caseComment")
  public String getCaseComment() {
    return caseComment;
  }

  public void setCaseComment(String caseComment) {
    this.caseComment = caseComment;
  }

  /**
   * Marks if the comment has been deleted from view in Case Management. The comment will not be
   * deleted from the audit trail of the Case or Alert. Capability to delete comments may not be
   * granted to all Users.
   **/

  @Schema(description = "Marks if the comment has been deleted from view in Case Management. The comment will not be deleted from the audit trail of the Case or Alert. Capability to delete comments may not be granted to all Users.")
  @JsonProperty("deletedFlag")
  public Integer getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  /**
   * Will display if access to the comment has been restricted, for example, Level 3 Only. If the
   * comment has not been restricted, this field will be displayed as blank.
   **/

  @Schema(description = "Will display if access to the comment has been restricted, for example, Level 3 Only. If the comment has not been restricted, this field will be displayed as blank.")
  @JsonProperty("restrictingPermission")
  public String getRestrictingPermission() {
    return restrictingPermission;
  }

  public void setRestrictingPermission(String restrictingPermission) {
    this.restrictingPermission = restrictingPermission;
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
    return Objects.equals(caseId, caseComments.caseId) &&
        Objects.equals(commentId, caseComments.commentId) &&
        Objects.equals(commentedBy, caseComments.commentedBy) &&
        Objects.equals(commentDateTime, caseComments.commentDateTime) &&
        Objects.equals(caseComment, caseComments.caseComment) &&
        Objects.equals(deletedFlag, caseComments.deletedFlag) &&
        Objects.equals(restrictingPermission, caseComments.restrictingPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, commentId, commentedBy, commentDateTime, caseComment, deletedFlag,
        restrictingPermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseComments {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    commentId: ").append(toIndentedString(commentId)).append("\n");
    sb.append("    commentedBy: ").append(toIndentedString(commentedBy)).append("\n");
    sb.append("    commentDateTime: ").append(toIndentedString(commentDateTime)).append("\n");
    sb.append("    caseComment: ").append(toIndentedString(caseComment)).append("\n");
    sb.append("    deletedFlag: ").append(toIndentedString(deletedFlag)).append("\n");
    sb
        .append("    restrictingPermission: ")
        .append(toIndentedString(restrictingPermission))
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
