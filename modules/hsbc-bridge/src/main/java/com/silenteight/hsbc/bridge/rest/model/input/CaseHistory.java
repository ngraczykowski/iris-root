package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * CaseHistory
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CaseHistory   {
  @JsonProperty("caseID")
  private Integer caseID = null;

  @JsonProperty("modifiedBy")
  private Integer modifiedBy = null;

  @JsonProperty("modifiedDateTime")
  private String modifiedDateTime = null;

  @JsonProperty("attribute")
  private String attribute = null;

  @JsonProperty("oldValue")
  private String oldValue = null;

  @JsonProperty("newValue")
  private String newValue = null;

  @JsonProperty("transition")
  private String transition = null;

  public CaseHistory caseID(Integer caseID) {
    this.caseID = caseID;
    return this;
  }

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   * @return caseID
   **/
  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  
    public Integer getCaseID() {
    return caseID;
  }

  public void setCaseID(Integer caseID) {
    this.caseID = caseID;
  }

  public CaseHistory modifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
    return this;
  }

  /**
   * Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification
   * @return modifiedBy
   **/
  @Schema(description = "Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification")
  
    public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public CaseHistory modifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
    return this;
  }

  /**
   * Date and time of the modification
   * @return modifiedDateTime
   **/
  @Schema(description = "Date and time of the modification")
  
    public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }

  public CaseHistory attribute(String attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Records what has changed within the Case or Alert
   * @return attribute
   **/
  @Schema(description = "Records what has changed within the Case or Alert")
  
    public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  public CaseHistory oldValue(String oldValue) {
    this.oldValue = oldValue;
    return this;
  }

  /**
   * Records the previous state of the Case or Alert, for example&#58; Level 1 Review
   * @return oldValue
   **/
  @Schema(description = "Records the previous state of the Case or Alert, for example&#58; Level 1 Review")
  
    public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  public CaseHistory newValue(String newValue) {
    this.newValue = newValue;
    return this;
  }

  /**
   * Records the new state of the Case or Alert, for example&#58; Discount False Positive
   * @return newValue
   **/
  @Schema(description = "Records the new state of the Case or Alert, for example&#58; Discount False Positive")
  
    public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  public CaseHistory transition(String transition) {
    this.transition = transition;
    return this;
  }

  /**
   * Records the transition used to move the alert through the workflow
   * @return transition
   **/
  @Schema(description = "Records the transition used to move the alert through the workflow")
  
    public String getTransition() {
    return transition;
  }

  public void setTransition(String transition) {
    this.transition = transition;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaseHistory caseHistory = (CaseHistory) o;
    return Objects.equals(this.caseID, caseHistory.caseID) &&
        Objects.equals(this.modifiedBy, caseHistory.modifiedBy) &&
        Objects.equals(this.modifiedDateTime, caseHistory.modifiedDateTime) &&
        Objects.equals(this.attribute, caseHistory.attribute) &&
        Objects.equals(this.oldValue, caseHistory.oldValue) &&
        Objects.equals(this.newValue, caseHistory.newValue) &&
        Objects.equals(this.transition, caseHistory.transition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseID, modifiedBy, modifiedDateTime, attribute, oldValue, newValue, transition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseHistory {\n");
    
    sb.append("    caseID: ").append(toIndentedString(caseID)).append("\n");
    sb.append("    modifiedBy: ").append(toIndentedString(modifiedBy)).append("\n");
    sb.append("    modifiedDateTime: ").append(toIndentedString(modifiedDateTime)).append("\n");
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    oldValue: ").append(toIndentedString(oldValue)).append("\n");
    sb.append("    newValue: ").append(toIndentedString(newValue)).append("\n");
    sb.append("    transition: ").append(toIndentedString(transition)).append("\n");
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
