package com.silenteight.sep.usermanagement.keycloak;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class KeycloakException extends RuntimeException {

  private static final long serialVersionUID = 3007391747112415014L;

  public KeycloakException(String message) {
    super(message);
  }
}
