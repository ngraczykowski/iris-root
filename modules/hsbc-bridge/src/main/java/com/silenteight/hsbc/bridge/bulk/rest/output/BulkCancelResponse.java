package com.silenteight.hsbc.bridge.bulk.rest.output;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * BulkCancelResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class BulkCancelResponse   {
  @JsonProperty("bulkId")
  private UUID bulkId = null;

  @JsonProperty("bulkStatus")
  private BulkStatus bulkStatus = null;

  public BulkCancelResponse bulkId(UUID bulkId) {
    this.bulkId = bulkId;
    return this;
  }

  /**
   * Get bulkId
   * @return bulkId
   **/
  @Schema(description = "")
  
    @Valid
    public UUID getBulkId() {
    return bulkId;
  }

  public void setBulkId(UUID bulkId) {
    this.bulkId = bulkId;
  }

  public BulkCancelResponse bulkStatus(BulkStatus bulkStatus) {
    this.bulkStatus = bulkStatus;
    return this;
  }

  /**
   * Get bulkStatus
   * @return bulkStatus
   **/
  @Schema(description = "")
  
    @Valid
    public BulkStatus getBulkStatus() {
    return bulkStatus;
  }

  public void setBulkStatus(BulkStatus bulkStatus) {
    this.bulkStatus = bulkStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BulkCancelResponse bulkCancelResponse = (BulkCancelResponse) o;
    return Objects.equals(this.bulkId, bulkCancelResponse.bulkId) &&
        Objects.equals(this.bulkStatus, bulkCancelResponse.bulkStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bulkId, bulkStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BulkCancelResponse {\n");
    
    sb.append("    bulkId: ").append(toIndentedString(bulkId)).append("\n");
    sb.append("    bulkStatus: ").append(toIndentedString(bulkStatus)).append("\n");
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
