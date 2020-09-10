package com.silenteight.sep.keycloak.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.keycloak.id.KeycloakUserIdProvider;

import com.sillenteight.sep.usermanagement.api.ResettableUserCredentials;
import com.sillenteight.sep.usermanagement.api.UserCredentialsRepository;
import org.keycloak.admin.client.resource.UsersResource;

import java.util.Optional;

@RequiredArgsConstructor
class KeycloakUserCredentialsRepository implements UserCredentialsRepository {

  private final UsersResource usersResource;
  private final KeycloakUserIdProvider keycloakUserIdProvider;

  @Override
  public Optional<ResettableUserCredentials> findUserCredentials(String username) {
    return keycloakUserIdProvider.findId(username)
        .map(usersResource::get)
        .map(KeycloakResettableUserCredentials::new);
  }
}
