package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CaseAttachments
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CaseAttachments   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("attachmentId")
  private Integer attachmentId = null;

  @JsonProperty("attachmentBy")
  private Integer attachmentBy = null;

  @JsonProperty("attachmentDateTime")
  private String attachmentDateTime = null;

  @JsonProperty("attachmentName")
  private String attachmentName = null;

  @JsonProperty("sourceName")
  private String sourceName = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("mimeType")
  private String mimeType = null;

  @JsonProperty("deletedFlag")
  private Integer deletedFlag = null;

  @JsonProperty("restrictingPermission")
  private String restrictingPermission = null;

  public CaseAttachments caseId(Integer caseId) {
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

  public CaseAttachments attachmentId(Integer attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Unique identifier added the attachment
   * @return attachmentId
   **/
  @Schema(description = "Unique identifier added the attachment")
  
    public Integer getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(Integer attachmentId) {
    this.attachmentId = attachmentId;
  }

  public CaseAttachments attachmentBy(Integer attachmentBy) {
    this.attachmentBy = attachmentBy;
    return this;
  }

  /**
   * Which User added the attachment
   * @return attachmentBy
   **/
  @Schema(description = "Which User added the attachment")
  
    public Integer getAttachmentBy() {
    return attachmentBy;
  }

  public void setAttachmentBy(Integer attachmentBy) {
    this.attachmentBy = attachmentBy;
  }

  public CaseAttachments attachmentDateTime(String attachmentDateTime) {
    this.attachmentDateTime = attachmentDateTime;
    return this;
  }

  /**
   * Date and time the attachment was added
   * @return attachmentDateTime
   **/
  @Schema(description = "Date and time the attachment was added")
  
    public String getAttachmentDateTime() {
    return attachmentDateTime;
  }

  public void setAttachmentDateTime(String attachmentDateTime) {
    this.attachmentDateTime = attachmentDateTime;
  }

  public CaseAttachments attachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
    return this;
  }

  /**
   * Name of the attachment.
   * @return attachmentName
   **/
  @Schema(description = "Name of the attachment.")
  
    public String getAttachmentName() {
    return attachmentName;
  }

  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }

  public CaseAttachments sourceName(String sourceName) {
    this.sourceName = sourceName;
    return this;
  }

  /**
   * Indicate the case source the attachment relates to. This will always be Sentry.
   * @return sourceName
   **/
  @Schema(description = "Indicate the case source the attachment relates to. This will always be Sentry.")
  
    public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public CaseAttachments description(String description) {
    this.description = description;
    return this;
  }

  /**
   * The descriotion given to the attachment by the end user
   * @return description
   **/
  @Schema(description = "The descriotion given to the attachment by the end user")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CaseAttachments mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Displays the source where the attachment originated
   * @return mimeType
   **/
  @Schema(description = "Displays the source where the attachment originated")
  
    public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public CaseAttachments deletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
    return this;
  }

  /**
   * Marks if the attachment has been deleted
   * @return deletedFlag
   **/
  @Schema(description = "Marks if the attachment has been deleted")
  
    public Integer getDeletedFlag() {
    return deletedFlag;
  }

  public void setDeletedFlag(Integer deletedFlag) {
    this.deletedFlag = deletedFlag;
  }

  public CaseAttachments restrictingPermission(String restrictingPermission) {
    this.restrictingPermission = restrictingPermission;
    return this;
  }

  /**
   * Will display if access to the attachment has been restricted, for example, Level 3 Only. If the attachment has not been restricted, this field will be displayed as blank.
   * @return restrictingPermission
   **/
  @Schema(description = "Will display if access to the attachment has been restricted, for example, Level 3 Only. If the attachment has not been restricted, this field will be displayed as blank.")
  
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
    CaseAttachments caseAttachments = (CaseAttachments) o;
    return Objects.equals(this.caseId, caseAttachments.caseId) &&
        Objects.equals(this.attachmentId, caseAttachments.attachmentId) &&
        Objects.equals(this.attachmentBy, caseAttachments.attachmentBy) &&
        Objects.equals(this.attachmentDateTime, caseAttachments.attachmentDateTime) &&
        Objects.equals(this.attachmentName, caseAttachments.attachmentName) &&
        Objects.equals(this.sourceName, caseAttachments.sourceName) &&
        Objects.equals(this.description, caseAttachments.description) &&
        Objects.equals(this.mimeType, caseAttachments.mimeType) &&
        Objects.equals(this.deletedFlag, caseAttachments.deletedFlag) &&
        Objects.equals(this.restrictingPermission, caseAttachments.restrictingPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, attachmentId, attachmentBy, attachmentDateTime, attachmentName, sourceName, description, mimeType, deletedFlag, restrictingPermission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseAttachments {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    attachmentBy: ").append(toIndentedString(attachmentBy)).append("\n");
    sb.append("    attachmentDateTime: ").append(toIndentedString(attachmentDateTime)).append("\n");
    sb.append("    attachmentName: ").append(toIndentedString(attachmentName)).append("\n");
    sb.append("    sourceName: ").append(toIndentedString(sourceName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
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
