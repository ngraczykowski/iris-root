package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FircoAuthenticationDto implements Serializable {

  private static final long serialVersionUID = -4365482316433926170L;

  /**
   * Firco Continuity Alert Review and Decision Workflow login. It must be declared and available in
   * Firco Continuity Alert Review and Decision Workflow.
   */
  @JsonProperty("ContinuityLogin")
  private String continuityLogin;

  /**
   * Firco Continuity Alert Review and Decision Workflow password. It must be encrypted using FKRUN
   * - Password.
   */
  @JsonProperty("ContinuityPassword")
  private String continuityPassword;

  /**
   * Always set to ROOT.
   */
  @JsonProperty("ContinuityBusinessUnit")
  private String continuityBusinessUnit = "ROOT";
}
