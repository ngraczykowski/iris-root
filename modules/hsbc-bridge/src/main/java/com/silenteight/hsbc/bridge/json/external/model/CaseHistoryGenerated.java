package com.silenteight.hsbc.bridge.json.external.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen", date = "2021-04-30T09:49:39.903Z[GMT]")
public class CaseHistoryGenerated {

  private String dnCASEHISTORYModifiedBy = null;
  private String dnCASEHISTORYModifiedDateTime = null;
  private String dnCASEHISTORYAttribute = null;
  private String dnCASEHISTORYOldValue = null;
  private String dnCASEHISTORYNewValue = null;
  private String dnCASEHISTORYTransition = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.modifiedBy")
  public String getDnCASEHISTORYModifiedBy() {
    return dnCASEHISTORYModifiedBy;
  }

  public void setDnCASEHISTORYModifiedBy(String dnCASEHISTORYModifiedBy) {
    this.dnCASEHISTORYModifiedBy = dnCASEHISTORYModifiedBy;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.modifiedDateTime")
  public String getDnCASEHISTORYModifiedDateTime() {
    return dnCASEHISTORYModifiedDateTime;
  }

  public void setDnCASEHISTORYModifiedDateTime(String dnCASEHISTORYModifiedDateTime) {
    this.dnCASEHISTORYModifiedDateTime = dnCASEHISTORYModifiedDateTime;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.attribute")
  public String getDnCASEHISTORYAttribute() {
    return dnCASEHISTORYAttribute;
  }

  public void setDnCASEHISTORYAttribute(String dnCASEHISTORYAttribute) {
    this.dnCASEHISTORYAttribute = dnCASEHISTORYAttribute;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.oldValue")
  public String getDnCASEHISTORYOldValue() {
    return dnCASEHISTORYOldValue;
  }

  public void setDnCASEHISTORYOldValue(String dnCASEHISTORYOldValue) {
    this.dnCASEHISTORYOldValue = dnCASEHISTORYOldValue;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.newValue")
  public String getDnCASEHISTORYNewValue() {
    return dnCASEHISTORYNewValue;
  }

  public void setDnCASEHISTORYNewValue(String dnCASEHISTORYNewValue) {
    this.dnCASEHISTORYNewValue = dnCASEHISTORYNewValue;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("DN_CASEHISTORY.transition")
  public String getDnCASEHISTORYTransition() {
    return dnCASEHISTORYTransition;
  }

  public void setDnCASEHISTORYTransition(String dnCASEHISTORYTransition) {
    this.dnCASEHISTORYTransition = dnCASEHISTORYTransition;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaseHistoryGenerated caseHistory = (CaseHistoryGenerated) o;
    return Objects.equals(dnCASEHISTORYModifiedBy, caseHistory.dnCASEHISTORYModifiedBy) &&
        Objects.equals(dnCASEHISTORYModifiedDateTime, caseHistory.dnCASEHISTORYModifiedDateTime) &&
        Objects.equals(dnCASEHISTORYAttribute, caseHistory.dnCASEHISTORYAttribute) &&
        Objects.equals(dnCASEHISTORYOldValue, caseHistory.dnCASEHISTORYOldValue) &&
        Objects.equals(dnCASEHISTORYNewValue, caseHistory.dnCASEHISTORYNewValue) &&
        Objects.equals(dnCASEHISTORYTransition, caseHistory.dnCASEHISTORYTransition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        dnCASEHISTORYModifiedBy, dnCASEHISTORYModifiedDateTime, dnCASEHISTORYAttribute,
        dnCASEHISTORYOldValue, dnCASEHISTORYNewValue, dnCASEHISTORYTransition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaseHistoryGenerated {\n");

    sb
        .append("    dnCASEHISTORYModifiedBy: ")
        .append(toIndentedString(dnCASEHISTORYModifiedBy))
        .append("\n");
    sb
        .append("    dnCASEHISTORYModifiedDateTime: ")
        .append(toIndentedString(dnCASEHISTORYModifiedDateTime))
        .append("\n");
    sb
        .append("    dnCASEHISTORYAttribute: ")
        .append(toIndentedString(dnCASEHISTORYAttribute))
        .append("\n");
    sb
        .append("    dnCASEHISTORYOldValue: ")
        .append(toIndentedString(dnCASEHISTORYOldValue))
        .append("\n");
    sb
        .append("    dnCASEHISTORYNewValue: ")
        .append(toIndentedString(dnCASEHISTORYNewValue))
        .append("\n");
    sb
        .append("    dnCASEHISTORYTransition: ")
        .append(toIndentedString(dnCASEHISTORYTransition))
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
