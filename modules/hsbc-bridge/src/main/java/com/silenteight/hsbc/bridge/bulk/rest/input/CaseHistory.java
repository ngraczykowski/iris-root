package com.silenteight.hsbc.bridge.bulk.rest.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CaseHistory {

  private int caseId;
  private Integer modifiedBy = null;
  private String modifiedDateTime = null;
  private String attribute = null;
  private String oldValue = null;
  private String newValue = null;
  private String transition = null;

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public int getCaseId() {
    return caseId;
  }

  public void setCaseId(int caseId) {
    this.caseId = caseId;
  }

  /**
   * Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or
   * Attachments constitute a modification
   **/

  @Schema(description = "Last User to modify the Case or Alert. Changes to the Case or Alert State, Comments or Attachments constitute a modification")
  @JsonProperty("modifiedBy")
  public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Date and time of the modification
   **/

  @Schema(description = "Date and time of the modification")
  @JsonProperty("modifiedDateTime")
  public String getModifiedDateTime() {
    return modifiedDateTime;
  }

  public void setModifiedDateTime(String modifiedDateTime) {
    this.modifiedDateTime = modifiedDateTime;
  }

  /**
   * Records what has changed within the Case or Alert
   **/

  @Schema(description = "Records what has changed within the Case or Alert")
  @JsonProperty("attribute")
  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  /**
   * Records the previous state of the Case or Alert, for example&amp;#58; Level 1 Review
   **/

  @Schema(description = "Records the previous state of the Case or Alert, for example&#58; Level 1 Review")
  @JsonProperty("oldValue")
  public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  /**
   * Records the new state of the Case or Alert, for example&amp;#58; Discount False Positive
   **/

  @Schema(description = "Records the new state of the Case or Alert, for example&#58; Discount False Positive")
  @JsonProperty("newValue")
  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  /**
   * Records the transition used to move the alert through the workflow
   **/

  @Schema(description = "Records the transition used to move the alert through the workflow")
  @JsonProperty("transition")
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
    return Objects.equals(caseId, caseHistory.caseId) &&
        Objects.equals(modifiedBy, caseHistory.modifiedBy) &&
        Objects.equals(modifiedDateTime, caseHistory.modifiedDateTime) &&
        Objects.equals(attribute, caseHistory.attribute) &&
        Objects.equals(oldValue, caseHistory.oldValue) &&
        Objects.equals(newValue, caseHistory.newValue) &&
        Objects.equals(transition, caseHistory.transition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, modifiedBy, modifiedDateTime, attribute, oldValue, newValue, transition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseHistory {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
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
