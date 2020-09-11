package com.silenteight.sep.usermanagement.keycloak.id;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;

@RequiredArgsConstructor
class SingleRequestKeycloakUserIdProvider implements KeycloakUserIdProvider {

  private final UsersResource usersResource;

  @Override
  public Optional<String> findId(String username) {
    return usersResource.search(username).stream()
        .map(UserRepresentation::getId)
        .findFirst();
  }
}
