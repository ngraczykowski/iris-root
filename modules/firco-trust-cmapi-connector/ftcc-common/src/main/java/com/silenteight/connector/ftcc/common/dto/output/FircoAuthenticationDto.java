package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class FircoAuthenticationDto {

  /**
   * Firco Continuity Alert Review and Decision Workflow login. It must be declared and available in
   * Firco Continuity Alert Review and Decision Workflow.
   */
  @JsonProperty("TrustLogin")
  String continuityLogin;

  /**
   * Firco Continuity Alert Review and Decision Workflow password. It must be encrypted using FKRUN
   * - Password.
   */
  @ToString.Exclude
  @JsonProperty("TrustPassword")
  String continuityPassword;

}
