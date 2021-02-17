package com.silenteight.hsbc.bridge.rest.model.output;

import com.silenteight.hsbc.bridge.rest.model.input.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.input.BulkStatus;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BulkStatusResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class BulkStatusResponse   {
  @JsonProperty("bulkId")
  private UUID bulkId = null;

  @JsonProperty("bulkStatus")
  private BulkStatus bulkStatus = null;

  @JsonProperty("requestedAlerts")
  @Valid
  private List<BulkAlertItem> requestedAlerts = null;

  public BulkStatusResponse bulkId(UUID bulkId) {
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

  public BulkStatusResponse bulkStatus(BulkStatus bulkStatus) {
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

  public BulkStatusResponse requestedAlerts(List<BulkAlertItem> requestedAlerts) {
    this.requestedAlerts = requestedAlerts;
    return this;
  }

  public BulkStatusResponse addRequestedAlertsItem(BulkAlertItem requestedAlertsItem) {
    if (this.requestedAlerts == null) {
      this.requestedAlerts = new ArrayList<BulkAlertItem>();
    }
    this.requestedAlerts.add(requestedAlertsItem);
    return this;
  }

  /**
   * Get requestedAlerts
   * @return requestedAlerts
   **/
  @Schema(description = "")
      @Valid
    public List<BulkAlertItem> getRequestedAlerts() {
    return requestedAlerts;
  }

  public void setRequestedAlerts(List<BulkAlertItem> requestedAlerts) {
    this.requestedAlerts = requestedAlerts;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BulkStatusResponse bulkStatusResponse = (BulkStatusResponse) o;
    return Objects.equals(this.bulkId, bulkStatusResponse.bulkId) &&
        Objects.equals(this.bulkStatus, bulkStatusResponse.bulkStatus) &&
        Objects.equals(this.requestedAlerts, bulkStatusResponse.requestedAlerts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bulkId, bulkStatus, requestedAlerts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BulkStatusResponse {\n");
    
    sb.append("    bulkId: ").append(toIndentedString(bulkId)).append("\n");
    sb.append("    bulkStatus: ").append(toIndentedString(bulkStatus)).append("\n");
    sb.append("    requestedAlerts: ").append(toIndentedString(requestedAlerts)).append("\n");
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
