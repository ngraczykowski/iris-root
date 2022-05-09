package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
@ToString
class ClientNotFoundException extends KeycloakException {

  private static final long serialVersionUID = -4228911808916368248L;

  ClientNotFoundException(String clientId) {
    super(format("Client with clientId=%s not found.", clientId));
  }
}
