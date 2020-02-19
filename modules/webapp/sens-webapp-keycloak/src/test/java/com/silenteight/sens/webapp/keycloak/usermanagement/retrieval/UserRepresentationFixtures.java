package com.silenteight.sens.webapp.keycloak.usermanagement.retrieval;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.keycloak.representations.idm.UserRepresentation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserRepresentationFixtures {

  static final UserRepresentation JOHN_SMITH =
      userRepresentation("dia9-vbaq-mbn9-07a8", "jsmith");

  private static UserRepresentation userRepresentation(String id, String username) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setId(id);
    userRepresentation.setUsername(username);
    return userRepresentation;
  }
}
