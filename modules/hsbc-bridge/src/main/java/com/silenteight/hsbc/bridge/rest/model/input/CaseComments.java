package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * CaseComments
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CaseComments   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("commentId")
  private Integer commentId = null;

  @JsonProperty("commentedBy")
  private Integer commentedBy = null;

  @JsonProperty("commentDateTime")
  private String commentDateTime = null;

  @JsonProperty("caseComment")
  private String caseComment = null;

  @JsonProperty("deletedFlag")
  private Integer deletedFlag = null;

  @JsonProperty("restrictingPermission")
  private String restrictingPermission = null;

  public CaseComments caseId(Integer caseId) {
    this.caseId = caseId;
    return this;
  }

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   * @return caseId
   **/
  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  
    public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
    this.caseId = caseId;
  }

  public CaseComments commentId(Integer commentId) {
    this.commentId = commentId;
    return this;
  }

  /**
   * Unique identifier assigned to a comment
   * @return commentId
   **/
  @Schema(description = "Unique identifier assigned to a comment")
  
    public Integer getCommentId() {
    return commentId;
  }

  public void setCommentId(Integer commentId) {
    this.commentId = commentId;
  }

  public CaseComments commentedBy(Integer commentedBy) {
    this.commentedBy = commentedBy;
    return this;
  }

  /**
   * Which User was the author of the comment
   * @return commentedBy
   **/
  @Schema(description = "Which User was the author of the comment")
  
    public Integer getCommentedBy() {
    return commentedBy;
  }

  public void setCommentedBy(Integer commentedBy) {
    this.commentedBy = commentedBy;
  }

  public CaseComments commentDateTime(String commentDateTime) {
    this.commentDateTime = commentDateTime;
    return this;
  }

  /**
   * Date and time the comment was made
   * @return commentDateTime
   **/
  @Schema(description = "Date and time the comment was made")
  
    public String getCommentDateTime() {
    return commentDateTime;
  }

  public void setCommentDateTime(String commentDateTime) {
    this.commentDateTime = commentDateTime;
  }

  public CaseComments caseComment(String caseComment) {
    this.caseComment = caseComment;
    return this;
  }

  /**
   * Displays the comment
   * @return caseComment
   **/
  @Schema(description = "Displays the comment")
  
    public String getCaseComment() {
    return caseComment;
  }

  public void setCaseComment(String caseComment) {
    this.caseComment = caseComment;
  }

  public CaseComments deletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
    return this;
  }

  /**
   * Marks if the comment has been deleted from view in Case Management. The comment will not be deleted from the audit trail of the Case or Alert. Capability to delete comments may not be granted to all Users.
   * @return deletedFlag
   **/
  @Schema(description = "Marks if the comment has been deleted from view in Case Management. The comment will not be deleted from the audit trail of the Case or Alert. Capability to delete comments may not be granted to all Users.")
  
    public Integer getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public CaseComments restrictingPermission(String restrictingPermission) {
    this.restrictingPermission = restrictingPermission;
    return this;
  }

  /**
   * Will display if access to the comment has been restricted, for example, Level 3 Only. If the comment has not been restricted, this field will be displayed as blank.
   * @return restrictingPermission
   **/
  @Schema(description = "Will display if access to the comment has been restricted, for example, Level 3 Only. If the comment has not been restricted, this field will be displayed as blank.")
  
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
    return Objects.equals(this.caseId, caseComments.caseId) &&
        Objects.equals(this.commentId, caseComments.commentId) &&
        Objects.equals(this.commentedBy, caseComments.commentedBy) &&
        Objects.equals(this.commentDateTime, caseComments.commentDateTime) &&
        Objects.equals(this.caseComment, caseComments.caseComment) &&
        Objects.equals(this.deletedFlag, caseComments.deletedFlag) &&
        Objects.equals(this.restrictingPermission, caseComments.restrictingPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, commentId, commentedBy, commentDateTime, caseComment, deletedFlag, restrictingPermission);
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
    sb.append("    restrictingPermission: ").append(toIndentedString(restrictingPermission)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
