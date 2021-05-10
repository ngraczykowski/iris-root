package com.silenteight.hsbc.bridge.json.external.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen", date = "2021-04-30T09:49:39.903Z[GMT]")
public class Relationships {

  private String relationshipsInputId = null;
  private String relationshipsInputName = null;
  private String relationshipsRecordId = null;
  private String relationshipsRelatedInputId = null;
  private String relationshipsRelatedInputName = null;
  private String relationshipsRelatedRecordId = null;
  private String relationshipsRuleName = null;
  private String relationshipsPriorityScore = null;
  private String relationshipsRelationshipHash = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Input Id")
  public String getRelationshipsInputId() {
    return relationshipsInputId;
  }

  public void setRelationshipsInputId(String relationshipsInputId) {
    this.relationshipsInputId = relationshipsInputId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Input Name")
  public String getRelationshipsInputName() {
    return relationshipsInputName;
  }

  public void setRelationshipsInputName(String relationshipsInputName) {
    this.relationshipsInputName = relationshipsInputName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Record Id")
  public String getRelationshipsRecordId() {
    return relationshipsRecordId;
  }

  public void setRelationshipsRecordId(String relationshipsRecordId) {
    this.relationshipsRecordId = relationshipsRecordId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Related Input Id")
  public String getRelationshipsRelatedInputId() {
    return relationshipsRelatedInputId;
  }

  public void setRelationshipsRelatedInputId(String relationshipsRelatedInputId) {
    this.relationshipsRelatedInputId = relationshipsRelatedInputId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Related Input Name")
  public String getRelationshipsRelatedInputName() {
    return relationshipsRelatedInputName;
  }

  public void setRelationshipsRelatedInputName(String relationshipsRelatedInputName) {
    this.relationshipsRelatedInputName = relationshipsRelatedInputName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Related Record Id")
  public String getRelationshipsRelatedRecordId() {
    return relationshipsRelatedRecordId;
  }

  public void setRelationshipsRelatedRecordId(String relationshipsRelatedRecordId) {
    this.relationshipsRelatedRecordId = relationshipsRelatedRecordId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Rule Name")
  public String getRelationshipsRuleName() {
    return relationshipsRuleName;
  }

  public void setRelationshipsRuleName(String relationshipsRuleName) {
    this.relationshipsRuleName = relationshipsRuleName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Priority Score")
  public String getRelationshipsPriorityScore() {
    return relationshipsPriorityScore;
  }

  public void setRelationshipsPriorityScore(String relationshipsPriorityScore) {
    this.relationshipsPriorityScore = relationshipsPriorityScore;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("Relationships.Relationship Hash")
  public String getRelationshipsRelationshipHash() {
    return relationshipsRelationshipHash;
  }

  public void setRelationshipsRelationshipHash(String relationshipsRelationshipHash) {
    this.relationshipsRelationshipHash = relationshipsRelationshipHash;
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
    return Objects.equals(relationshipsInputId, relationships.relationshipsInputId) &&
        Objects.equals(relationshipsInputName, relationships.relationshipsInputName) &&
        Objects.equals(relationshipsRecordId, relationships.relationshipsRecordId) &&
        Objects.equals(relationshipsRelatedInputId, relationships.relationshipsRelatedInputId) &&
        Objects.equals(relationshipsRelatedInputName, relationships.relationshipsRelatedInputName)
        &&
        Objects.equals(relationshipsRelatedRecordId, relationships.relationshipsRelatedRecordId) &&
        Objects.equals(relationshipsRuleName, relationships.relationshipsRuleName) &&
        Objects.equals(relationshipsPriorityScore, relationships.relationshipsPriorityScore) &&
        Objects.equals(relationshipsRelationshipHash, relationships.relationshipsRelationshipHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        relationshipsInputId, relationshipsInputName, relationshipsRecordId,
        relationshipsRelatedInputId, relationshipsRelatedInputName, relationshipsRelatedRecordId,
        relationshipsRuleName, relationshipsPriorityScore, relationshipsRelationshipHash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Relationships {\n");

    sb
        .append("    relationshipsInputId: ")
        .append(toIndentedString(relationshipsInputId))
        .append("\n");
    sb
        .append("    relationshipsInputName: ")
        .append(toIndentedString(relationshipsInputName))
        .append("\n");
    sb
        .append("    relationshipsRecordId: ")
        .append(toIndentedString(relationshipsRecordId))
        .append("\n");
    sb
        .append("    relationshipsRelatedInputId: ")
        .append(toIndentedString(relationshipsRelatedInputId))
        .append("\n");
    sb
        .append("    relationshipsRelatedInputName: ")
        .append(toIndentedString(relationshipsRelatedInputName))
        .append("\n");
    sb
        .append("    relationshipsRelatedRecordId: ")
        .append(toIndentedString(relationshipsRelatedRecordId))
        .append("\n");
    sb
        .append("    relationshipsRuleName: ")
        .append(toIndentedString(relationshipsRuleName))
        .append("\n");
    sb
        .append("    relationshipsPriorityScore: ")
        .append(toIndentedString(relationshipsPriorityScore))
        .append("\n");
    sb
        .append("    relationshipsRelationshipHash: ")
        .append(toIndentedString(relationshipsRelationshipHash))
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
