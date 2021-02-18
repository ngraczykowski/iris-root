package com.silenteight.hsbc.bridge.rest.model.output;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * BulkAcceptedResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class BulkAcceptedResponse   {
  @JsonProperty("bulkId")
  private UUID bulkId = null;

  @JsonProperty("requestedAlerts")
  @Valid
  private List<BulkAlertItem> requestedAlerts = null;

  public BulkAcceptedResponse bulkId(UUID bulkId) {
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

  public BulkAcceptedResponse requestedAlerts(List<BulkAlertItem> requestedAlerts) {
    this.requestedAlerts = requestedAlerts;
    return this;
  }

  public BulkAcceptedResponse addRequestedAlertsItem(BulkAlertItem requestedAlertsItem) {
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
    BulkAcceptedResponse bulkAcceptedResponse = (BulkAcceptedResponse) o;
    return Objects.equals(this.bulkId, bulkAcceptedResponse.bulkId) &&
        Objects.equals(this.requestedAlerts, bulkAcceptedResponse.requestedAlerts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bulkId, requestedAlerts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BulkAcceptedResponse {\n");
    
    sb.append("    bulkId: ").append(toIndentedString(bulkId)).append("\n");
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
