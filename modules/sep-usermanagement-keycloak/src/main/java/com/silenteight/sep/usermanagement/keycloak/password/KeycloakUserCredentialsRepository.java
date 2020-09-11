package com.silenteight.sep.usermanagement.keycloak.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.ResettableUserCredentials;
import com.silenteight.sep.usermanagement.api.UserCredentialsRepository;
import com.silenteight.sep.usermanagement.keycloak.id.KeycloakUserIdProvider;

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
