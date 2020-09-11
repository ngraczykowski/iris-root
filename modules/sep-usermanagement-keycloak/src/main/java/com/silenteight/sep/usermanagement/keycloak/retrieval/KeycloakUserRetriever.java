package com.silenteight.sep.usermanagement.keycloak.retrieval;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

@RequiredArgsConstructor
public class KeycloakUserRetriever {

  private final UsersResource usersResource;

  public UserResource retrieve(String username) {
    return usersResource
        .search(username)
        .stream()
        .findFirst()
        .map(UserRepresentation::getId)
        .map(usersResource::get)
        .orElseThrow(() -> new KeycloakUserNotFoundException(username));
  }
}
