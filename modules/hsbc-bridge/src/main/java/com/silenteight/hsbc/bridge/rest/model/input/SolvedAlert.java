package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SolvedAlert
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class SolvedAlert   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("apName")
  private String apName = null;

  @JsonProperty("wlName")
  private String wlName = null;

  @JsonProperty("recommendation")
  private SolvedAlertStatus recommendation = null;

  @JsonProperty("comment")
  private String comment = null;

  public SolvedAlert id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  
    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SolvedAlert apName(String apName) {
    this.apName = apName;
    return this;
  }

  /**
   * Get apName
   * @return apName
   **/
  @Schema(description = "")
  
    public String getApName() {
    return apName;
  }

  public void setApName(String apName) {
    this.apName = apName;
  }

  public SolvedAlert wlName(String wlName) {
    this.wlName = wlName;
    return this;
  }

  /**
   * Get wlName
   * @return wlName
   **/
  @Schema(description = "")
  
    public String getWlName() {
    return wlName;
  }

  public void setWlName(String wlName) {
    this.wlName = wlName;
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
  
    @Valid
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
        Objects.equals(this.apName, solvedAlert.apName) &&
        Objects.equals(this.wlName, solvedAlert.wlName) &&
        Objects.equals(this.recommendation, solvedAlert.recommendation) &&
        Objects.equals(this.comment, solvedAlert.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, apName, wlName, recommendation, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SolvedAlert {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    apName: ").append(toIndentedString(apName)).append("\n");
    sb.append("    wlName: ").append(toIndentedString(wlName)).append("\n");
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
