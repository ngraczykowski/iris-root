package com.silenteight.sep.usermanagement.keycloak.retrieval;


import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

class KeycloakUserNotFoundException extends KeycloakException {

  private static final long serialVersionUID = -7893728977980855767L;

  KeycloakUserNotFoundException(String username) {
    super("User '" + username + "' could not be found");
  }
}
