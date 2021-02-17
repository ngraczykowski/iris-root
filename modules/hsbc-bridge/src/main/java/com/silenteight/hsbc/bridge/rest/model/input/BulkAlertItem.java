package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * BulkAlertItem
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class BulkAlertItem   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("status")
  private BulkAlertStatus status = null;

  @JsonProperty("errorMessage")
  private String errorMessage = null;

  public BulkAlertItem id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  
    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public BulkAlertItem status(BulkAlertStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  @Schema(description = "")
  
    @Valid
    public BulkAlertStatus getStatus() {
    return status;
  }

  public void setStatus(BulkAlertStatus status) {
    this.status = status;
  }

  public BulkAlertItem errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  /**
   * Get errorMessage
   * @return errorMessage
   **/
  @Schema(description = "")
  
    public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BulkAlertItem bulkAlertItem = (BulkAlertItem) o;
    return Objects.equals(this.id, bulkAlertItem.id) &&
        Objects.equals(this.status, bulkAlertItem.status) &&
        Objects.equals(this.errorMessage, bulkAlertItem.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, errorMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BulkAlertItem {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
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
