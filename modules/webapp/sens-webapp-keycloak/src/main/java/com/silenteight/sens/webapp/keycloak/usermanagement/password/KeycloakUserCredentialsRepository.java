package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.keycloak.usermanagement.id.KeycloakUserIdProvider;
import com.silenteight.sens.webapp.user.password.reset.ResettableUserCredentials;
import com.silenteight.sens.webapp.user.password.reset.UserCredentialsRepository;

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
