package com.silenteight.hsbc.bridge.rest.model.output;

import com.silenteight.hsbc.bridge.rest.model.input.SolvedAlert;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BulkSolvedAlertsResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class BulkSolvedAlertsResponse   {
  @JsonProperty("alerts")
  @Valid
  private List<SolvedAlert> alerts = null;

  public BulkSolvedAlertsResponse alerts(List<SolvedAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public BulkSolvedAlertsResponse addAlertsItem(SolvedAlert alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<SolvedAlert>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   **/
  @Schema(description = "")
      @Valid
    public List<SolvedAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<SolvedAlert> alerts) {
    this.alerts = alerts;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BulkSolvedAlertsResponse bulkSolvedAlertsResponse = (BulkSolvedAlertsResponse) o;
    return Objects.equals(this.alerts, bulkSolvedAlertsResponse.alerts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alerts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BulkSolvedAlertsResponse {\n");
    
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
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
