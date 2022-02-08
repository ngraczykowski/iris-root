package com.silenteight.sep.usermanagement.keycloak.remove;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.user.UserRemover;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.keycloak.admin.client.resource.UserResource;

@RequiredArgsConstructor
public class KeycloakUserRemover implements UserRemover {

  private final KeycloakUserRetriever keycloakUserRetriever;

  @Override
  public void remove(String username) {
    UserResource userResource = keycloakUserRetriever.retrieve(username);

    userResource.remove();
  }
}
