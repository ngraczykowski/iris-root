package com.silenteight.hsbc.bridge.bulk.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ErrorAlert {

  @JsonProperty("id")
  @NotNull
  @NotBlank
  private String id = null;

  @JsonProperty("errorMessage")
  private String errorMessage= null;

  @JsonProperty("alertMetadata")
  @NotNull
  @Valid
  private List<AlertMetadata> alertMetadata = null;

  public ErrorAlert id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   **/
  @Schema(description = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ErrorAlert errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  @Schema(description = "")
  public String getErrorMessage() { return errorMessage; }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ErrorAlert alertMetadata(List<AlertMetadata> alertMetadata) {
    this.alertMetadata = alertMetadata;
    return this;
  }

  public ErrorAlert addAlertMetadataItem(AlertMetadata alertMetadataItem) {
    if (this.alertMetadata == null) {
      this.alertMetadata = new ArrayList<AlertMetadata>();
    }
    this.alertMetadata.add(alertMetadataItem);
    return this;
  }

  /**
   * Get alertMetadata
   *
   * @return alertMetadata
   **/
  @Schema(description = "")
  public List<AlertMetadata> getAlertMetadata() {
    return alertMetadata;
  }

  public void setAlertMetadata(List<AlertMetadata> alertMetadata) {
    this.alertMetadata = alertMetadata;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorAlert errorAlert = (ErrorAlert) o;
    return Objects.equals(this.id, errorAlert.id) &&
        Objects.equals(this.errorMessage, errorAlert.errorMessage) &&
        Objects.equals(this.alertMetadata, errorAlert.alertMetadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, errorMessage, alertMetadata);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SolvedAlert {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    alertMetadata: ").append(toIndentedString(alertMetadata)).append("\n");
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
