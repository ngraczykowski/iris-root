package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAuthenticationDto implements Serializable {

  private static final long serialVersionUID = 1172622409389133221L;
  @JsonProperty("UserLogin")
  String userLogin;
  @JsonProperty("UserPassword")
  String userPassword;
  @JsonProperty("UserRealm")
  String userRealm;
}
