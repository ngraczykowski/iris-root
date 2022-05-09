package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import javax.ws.rs.core.Response;

@EqualsAndHashCode(callSuper = true)
@ToString
class CreateUserException extends KeycloakException {

  private static final long serialVersionUID = 3007391747112415014L;

  CreateUserException(Response response) {
    super(response.getStatusInfo().getReasonPhrase());
  }
}
