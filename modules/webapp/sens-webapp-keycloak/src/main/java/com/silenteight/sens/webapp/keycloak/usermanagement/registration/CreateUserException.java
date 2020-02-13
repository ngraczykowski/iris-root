package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sens.webapp.keycloak.KeycloakException;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
@ToString
class CreateUserException extends KeycloakException {

  private static final long serialVersionUID = 3007391747112415014L;

  CreateUserException(Response response) {
    super(response.getStatusInfo().getReasonPhrase());
  }
}
