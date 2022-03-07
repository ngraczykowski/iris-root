package com.silenteight.connector.ftcc.ingest.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class FircoAuthenticationDto implements Serializable {

  private static final long serialVersionUID = -4365482316433926170L;

  /**
   * Firco Continuity Alert Review and Decision Workflow login. It must be declared and available in
   * Firco Continuity Alert Review and Decision Workflow.
   */
  @JsonProperty("TrustLogin")
  private String continuityLogin;

  /**
   * Firco Continuity Alert Review and Decision Workflow password. It must be encrypted using FKRUN
   * - Password.
   */
  @ToString.Exclude
  @JsonProperty("TrustPassword")
  private String continuityPassword;

  /**
   * Always set to ROOT.
   */
  private String continuityBusinessUnit = "ROOT";
}
