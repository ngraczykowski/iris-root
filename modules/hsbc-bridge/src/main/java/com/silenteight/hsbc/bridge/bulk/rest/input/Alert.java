package com.silenteight.hsbc.bridge.bulk.rest.input;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class Alert {

  private AlertSystemInformation systemInformation = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("systemInformation")
  public AlertSystemInformation getSystemInformation() {
    return systemInformation;
  }

  public void setSystemInformation(AlertSystemInformation systemInformation) {
    this.systemInformation = systemInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Alert alert = (Alert) o;
    return Objects.equals(systemInformation, alert.systemInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(systemInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alert {\n");

    sb.append("    systemInformation: ").append(toIndentedString(systemInformation)).append("\n");
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
