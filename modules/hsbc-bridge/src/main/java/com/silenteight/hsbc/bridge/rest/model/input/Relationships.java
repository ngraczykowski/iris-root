package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * Relationships
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class Relationships   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("inputId")
  private BigDecimal inputId = null;

  @JsonProperty("inputName")
  private String inputName = null;

  @JsonProperty("recordID")
  private BigDecimal recordID = null;

  @JsonProperty("relatedInputId")
  private BigDecimal relatedInputId = null;

  @JsonProperty("relatedInputName")
  private String relatedInputName = null;

  @JsonProperty("relatedRecordId")
  private BigDecimal relatedRecordId = null;

  @JsonProperty("ruleName")
  private String ruleName = null;

  @JsonProperty("priorityScore")
  private BigDecimal priorityScore = null;

  @JsonProperty("relationshipHash")
  private String relationshipHash = null;

  public Relationships caseId(Integer caseId) {
    this.caseId = caseId;
    return this;
  }

  /**
   * Unique identifier assigned to the Case or Alert within Case Management
   * @return caseId
   **/
  @Schema(description = "Unique identifier assigned to the Case or Alert within Case Management")
  
    public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
    this.caseId = caseId;
  }

  public Relationships inputId(BigDecimal inputId) {
    this.inputId = inputId;
    return this;
  }

  /**
   * Identifier for the type of data inputted from the Screening Subject
   * @return inputId
   **/
  @Schema(description = "Identifier for the type of data inputted from the Screening Subject")
  
    @Valid
    public BigDecimal getInputId() {
    return inputId;
  }

  public void setInputId(BigDecimal inputId) {
    this.inputId = inputId;
  }

  public Relationships inputName(String inputName) {
    this.inputName = inputName;
    return this;
  }

  /**
   * Describes the source of the input data, for example&#58; Customer Data
   * @return inputName
   **/
  @Schema(description = "Describes the source of the input data, for example&#58; Customer Data")
  
    public String getInputName() {
    return inputName;
  }

  public void setInputName(String inputName) {
    this.inputName = inputName;
  }

  public Relationships recordID(BigDecimal recordID) {
    this.recordID = recordID;
    return this;
  }

  /**
   * Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.
   * @return recordID
   **/
  @Schema(description = "Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.")
  
    @Valid
    public BigDecimal getRecordID() {
    return recordID;
  }

  public void setRecordID(BigDecimal recordID) {
    this.recordID = recordID;
  }

  public Relationships relatedInputId(BigDecimal relatedInputId) {
    this.relatedInputId = relatedInputId;
    return this;
  }

  /**
   * Identifier for the type of data input from the Watchlist
   * @return relatedInputId
   **/
  @Schema(description = "Identifier for the type of data input from the Watchlist")
  
    @Valid
    public BigDecimal getRelatedInputId() {
    return relatedInputId;
  }

  public void setRelatedInputId(BigDecimal relatedInputId) {
    this.relatedInputId = relatedInputId;
  }

  public Relationships relatedInputName(String relatedInputName) {
    this.relatedInputName = relatedInputName;
    return this;
  }

  /**
   * Describes the source of the input data, for example&#58; WorldCheck_Entities
   * @return relatedInputName
   **/
  @Schema(description = "Describes the source of the input data, for example&#58; WorldCheck_Entities")
  
    public String getRelatedInputName() {
    return relatedInputName;
  }

  public void setRelatedInputName(String relatedInputName) {
    this.relatedInputName = relatedInputName;
  }

  public Relationships relatedRecordId(BigDecimal relatedRecordId) {
    this.relatedRecordId = relatedRecordId;
    return this;
  }

  /**
   * Refers to a Watchlist record within an Alert where there are multiple Watchlist records within the same Alert.
   * @return relatedRecordId
   **/
  @Schema(description = "Refers to a Watchlist record within an Alert where there are multiple Watchlist records within the same Alert.")
  
    @Valid
    public BigDecimal getRelatedRecordId() {
    return relatedRecordId;
  }

  public void setRelatedRecordId(BigDecimal relatedRecordId) {
    this.relatedRecordId = relatedRecordId;
  }

  public Relationships ruleName(String ruleName) {
    this.ruleName = ruleName;
    return this;
  }

  /**
   * Denotes the Match Rule the Alert was generated against
   * @return ruleName
   **/
  @Schema(description = "Denotes the Match Rule the Alert was generated against")
  
    public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public Relationships priorityScore(BigDecimal priorityScore) {
    this.priorityScore = priorityScore;
    return this;
  }

  /**
   * Records the associated Match Rule priority score
   * @return priorityScore
   **/
  @Schema(description = "Records the associated Match Rule priority score")
  
    @Valid
    public BigDecimal getPriorityScore() {
    return priorityScore;
  }

  public void setPriorityScore(BigDecimal priorityScore) {
    this.priorityScore = priorityScore;
  }

  public Relationships relationshipHash(String relationshipHash) {
    this.relationshipHash = relationshipHash;
    return this;
  }

  /**
   * Hash of the relationship value
   * @return relationshipHash
   **/
  @Schema(description = "Hash of the relationship value")
  
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
    return Objects.equals(this.caseId, relationships.caseId) &&
        Objects.equals(this.inputId, relationships.inputId) &&
        Objects.equals(this.inputName, relationships.inputName) &&
        Objects.equals(this.recordID, relationships.recordID) &&
        Objects.equals(this.relatedInputId, relationships.relatedInputId) &&
        Objects.equals(this.relatedInputName, relationships.relatedInputName) &&
        Objects.equals(this.relatedRecordId, relationships.relatedRecordId) &&
        Objects.equals(this.ruleName, relationships.ruleName) &&
        Objects.equals(this.priorityScore, relationships.priorityScore) &&
        Objects.equals(this.relationshipHash, relationships.relationshipHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, inputId, inputName, recordID, relatedInputId, relatedInputName, relatedRecordId, ruleName, priorityScore, relationshipHash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Relationships {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    inputId: ").append(toIndentedString(inputId)).append("\n");
    sb.append("    inputName: ").append(toIndentedString(inputName)).append("\n");
    sb.append("    recordID: ").append(toIndentedString(recordID)).append("\n");
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
