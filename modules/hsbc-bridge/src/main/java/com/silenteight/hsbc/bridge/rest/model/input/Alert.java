package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Alert
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class Alert   {
  @JsonProperty("userInformation")
  private AlertUserInformation userInformation = null;

  @JsonProperty("systemInformation")
  private AlertSystemInformation systemInformation = null;

  public Alert userInformation(AlertUserInformation userInformation) {
    this.userInformation = userInformation;
    return this;
  }

  /**
   * Get userInformation
   * @return userInformation
   **/
  @Schema(description = "")
  
    @Valid
    public AlertUserInformation getUserInformation() {
    return userInformation;
  }

  public void setUserInformation(AlertUserInformation userInformation) {
    this.userInformation = userInformation;
  }

  public Alert systemInformation(AlertSystemInformation systemInformation) {
    this.systemInformation = systemInformation;
    return this;
  }

  /**
   * Get systemInformation
   * @return systemInformation
   **/
  @Schema(description = "")
  
    @Valid
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
    return Objects.equals(this.userInformation, alert.userInformation) &&
        Objects.equals(this.systemInformation, alert.systemInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userInformation, systemInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alert {\n");
    
    sb.append("    userInformation: ").append(toIndentedString(userInformation)).append("\n");
    sb.append("    systemInformation: ").append(toIndentedString(systemInformation)).append("\n");
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
