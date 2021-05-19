package com.silenteight.sep.usermanagement.keycloak.query.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
@ToString
class ClientNotFoundException extends KeycloakException {

  ClientNotFoundException(String clientId) {
    super(format("Client with clientId=%s not found.", clientId));
  }
}
