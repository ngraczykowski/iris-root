package com.silenteight.sens.webapp.keycloak;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
@ToString
public class KeycloakException extends RuntimeException {

  private static final long serialVersionUID = 3007391747112415014L;

  public KeycloakException(Response response) {
    super(response.getStatusInfo().getReasonPhrase());
  }
}
