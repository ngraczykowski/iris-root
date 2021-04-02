package com.silenteight.hsbc.bridge.bulk.rest.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class Relationships {

  private int caseId;
  private BigDecimal inputId = null;
  private String inputName = null;
  private BigDecimal recordId = null;
  private BigDecimal relatedInputId = null;
  private String relatedInputName = null;
  private BigDecimal relatedRecordId = null;
  private String ruleName = null;
  private BigDecimal priorityScore = null;
  private String relationshipHash = null;

  /**
   * Unique identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public int getCaseId() {
    return caseId;
  }

  public void setCaseId(int caseId) {
    this.caseId = caseId;
  }

  /**
   * Identifier for the type of data inputted from the Screening Subject
   **/

  @Schema(description = "Identifier for the type of data inputted from the Screening Subject")
  @JsonProperty("inputId")
  public BigDecimal getInputId() {
    return inputId;
  }

  public void setInputId(BigDecimal inputId) {
    this.inputId = inputId;
  }

  /**
   * Describes the source of the input data, for example&amp;#58; Customer Data
   **/

  @Schema(description = "Describes the source of the input data, for example&#58; Customer Data")
  @JsonProperty("inputName")
  public String getInputName() {
    return inputName;
  }

  public void setInputName(String inputName) {
    this.inputName = inputName;
  }

  /**
   * Refers to a Customer name record within an Alert where there are multiple Customer name records
   * within the same Alert. This is the working record ID.
   **/

  @Schema(description = "Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.")
  @JsonProperty("recordId")
  public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  /**
   * Identifier for the type of data input from the Watchlist
   **/

  @Schema(description = "Identifier for the type of data input from the Watchlist")
  @JsonProperty("relatedInputId")
  public BigDecimal getRelatedInputId() {
    return relatedInputId;
  }

  public void setRelatedInputId(BigDecimal relatedInputId) {
    this.relatedInputId = relatedInputId;
  }

  /**
   * Describes the source of the input data, for example&amp;#58; WorldCheck_Entities
   **/

  @Schema(description = "Describes the source of the input data, for example&#58; WorldCheck_Entities")
  @JsonProperty("relatedInputName")
  public String getRelatedInputName() {
    return relatedInputName;
  }

  public void setRelatedInputName(String relatedInputName) {
    this.relatedInputName = relatedInputName;
  }

  /**
   * Refers to a Watchlist record within an Alert where there are multiple Watchlist records within
   * the same Alert.
   **/

  @Schema(description = "Refers to a Watchlist record within an Alert where there are multiple Watchlist records within the same Alert.")
  @JsonProperty("relatedRecordId")
  public BigDecimal getRelatedRecordId() {
    return relatedRecordId;
  }

  public void setRelatedRecordId(BigDecimal relatedRecordId) {
    this.relatedRecordId = relatedRecordId;
  }

  /**
   * Denotes the Match Rule the Alert was generated against
   **/

  @Schema(description = "Denotes the Match Rule the Alert was generated against")
  @JsonProperty("ruleName")
  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  /**
   * Records the associated Match Rule priority score
   **/

  @Schema(description = "Records the associated Match Rule priority score")
  @JsonProperty("priorityScore")
  public BigDecimal getPriorityScore() {
    return priorityScore;
  }

  public void setPriorityScore(BigDecimal priorityScore) {
    this.priorityScore = priorityScore;
  }

  /**
   * Hash of the relationship value
   **/

  @Schema(description = "Hash of the relationship value")
  @JsonProperty("relationshipHash")
  public String getRelationshipHash() {
    return relationshipHash;
  }

  public void setRelationshipHash(String relationshipHash) {
    this.relationshipHash = relationshipHash;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Relationships relationships = (Relationships) o;
    return Objects.equals(caseId, relationships.caseId) &&
        Objects.equals(inputId, relationships.inputId) &&
        Objects.equals(inputName, relationships.inputName) &&
        Objects.equals(recordId, relationships.recordId) &&
        Objects.equals(relatedInputId, relationships.relatedInputId) &&
        Objects.equals(relatedInputName, relationships.relatedInputName) &&
        Objects.equals(relatedRecordId, relationships.relatedRecordId) &&
        Objects.equals(ruleName, relationships.ruleName) &&
        Objects.equals(priorityScore, relationships.priorityScore) &&
        Objects.equals(relationshipHash, relationships.relationshipHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, inputId, inputName, recordId, relatedInputId, relatedInputName, relatedRecordId,
        ruleName, priorityScore, relationshipHash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Relationships {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    inputId: ").append(toIndentedString(inputId)).append("\n");
    sb.append("    inputName: ").append(toIndentedString(inputName)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    relatedInputId: ").append(toIndentedString(relatedInputId)).append("\n");
    sb.append("    relatedInputName: ").append(toIndentedString(relatedInputName)).append("\n");
    sb.append("    relatedRecordId: ").append(toIndentedString(relatedRecordId)).append("\n");
    sb.append("    ruleName: ").append(toIndentedString(ruleName)).append("\n");
    sb.append("    priorityScore: ").append(toIndentedString(priorityScore)).append("\n");
    sb.append("    relationshipHash: ").append(toIndentedString(relationshipHash)).append("\n");
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
