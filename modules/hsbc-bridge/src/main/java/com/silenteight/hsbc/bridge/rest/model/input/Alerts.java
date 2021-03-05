package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class Alerts {

  private List<Alert> alerts = new ArrayList<Alert>();

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("alerts")
  public List<Alert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<Alert> alerts) {
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
    Alerts alerts = (Alerts) o;
    return Objects.equals(alerts, alerts.alerts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alerts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alerts {\n");

    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
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
