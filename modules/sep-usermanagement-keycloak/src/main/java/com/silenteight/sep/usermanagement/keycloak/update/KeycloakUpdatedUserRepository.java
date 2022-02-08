package com.silenteight.sep.usermanagement.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.user.UserUpdater;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;


@RequiredArgsConstructor
class KeycloakUpdatedUserRepository implements UserUpdater {

  @NonNull
  private final KeycloakUserUpdater keycloakUserUpdater;


  @Override
  public void update(@NonNull UpdateUserCommand command) {
    try {
      keycloakUserUpdater.update(command);
    } catch (Exception e) {
      throw UserUpdateException.of(command.getUsername(), e);
    }
  }
}
