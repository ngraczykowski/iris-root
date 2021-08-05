package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseManagerAuthenticationDto implements Serializable {

  private static final long serialVersionUID = 1172622409389133221L;

  /**
   * Case manager login.
   */
  @JsonProperty("UserLogin")
  private String userLogin;

  /**
   * Case manager password.
   */
  @JsonProperty("UserPassword")
  @ToString.Exclude
  private String userPassword;

  /**
   * Case manager realm.
   */
  @JsonProperty("UserRealm")
  private String userRealm;
}
