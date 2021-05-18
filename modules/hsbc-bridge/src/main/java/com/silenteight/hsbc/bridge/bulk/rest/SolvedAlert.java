/*
 * HSCB-Bridge-bulk API
 * HSCB-Bridge-bulk API
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.silenteight.hsbc.bridge.bulk.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
/**
 * SolvedAlert
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-04-02T11:57:31.043Z[GMT]")
@JsonInclude(Include.NON_NULL)
public class SolvedAlert {
  @SerializedName("id")
  private String id = null;

  @SerializedName("recommendation")
  private SolvedAlertStatus recommendation = null;

  @SerializedName("comment")
  private String comment = null;

  public SolvedAlert id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @Schema(description = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SolvedAlert recommendation(SolvedAlertStatus recommendation) {
    this.recommendation = recommendation;
    return this;
  }

   /**
   * Get recommendation
   * @return recommendation
  **/
  @Schema(description = "")
  public SolvedAlertStatus getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(SolvedAlertStatus recommendation) {
    this.recommendation = recommendation;
  }

  public SolvedAlert comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @Schema(description = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SolvedAlert solvedAlert = (SolvedAlert) o;
    return Objects.equals(this.id, solvedAlert.id) &&
        Objects.equals(this.recommendation, solvedAlert.recommendation) &&
        Objects.equals(this.comment, solvedAlert.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, recommendation, comment);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SolvedAlert {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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
