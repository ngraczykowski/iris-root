package com.silenteight.sep.usermanagement.keycloak.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsQuery;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsResetter;
import com.silenteight.sep.usermanagement.keycloak.id.KeycloakUserIdProvider;

import org.keycloak.admin.client.resource.UsersResource;

import java.util.Optional;

@RequiredArgsConstructor
class KeycloakUserCredentialsRepository implements UserCredentialsQuery {

  private final UsersResource usersResource;
  private final KeycloakUserIdProvider keycloakUserIdProvider;

  @Override
  public Optional<UserCredentialsResetter> findByUsername(String username) {
    return keycloakUserIdProvider.findId(username)
        .map(usersResource::get)
        .map(KeycloakResettableUserCredentials::new);
  }
}
