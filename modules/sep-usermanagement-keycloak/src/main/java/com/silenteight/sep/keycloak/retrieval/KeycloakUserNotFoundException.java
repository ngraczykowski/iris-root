package com.silenteight.sep.keycloak.retrieval;


import com.silenteight.sep.keycloak.KeycloakException;

class KeycloakUserNotFoundException extends KeycloakException {

  private static final long serialVersionUID = -7893728977980855767L;

  KeycloakUserNotFoundException(String username) {
    super("User '" + username + "' could not be found");
  }
}
