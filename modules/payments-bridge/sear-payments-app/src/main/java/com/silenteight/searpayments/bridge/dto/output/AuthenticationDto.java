package com.silenteight.searpayments.bridge.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto implements Serializable {

  private static final long serialVersionUID = -4365482316433926170L;
  @JsonProperty("ContinuityLogin")
  String continuityLogin;

  @JsonProperty("ContinuityPassword")
  String continuityPassword;

  @JsonProperty("ContinuityBusinessUnit")
  String continuityBuinessUnit;
}
