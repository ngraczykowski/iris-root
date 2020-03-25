package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.update.UpdatedUser;
import com.silenteight.sens.webapp.user.update.UpdatedUserRepository;

@RequiredArgsConstructor
class KeycloakUpdatedUserRepository implements UpdatedUserRepository {

  @NonNull
  private final KeycloakUserUpdater keycloakUserUpdater;

  @Override
  public void save(UpdatedUser updatedUser) throws UserUpdateException {
    try {
      keycloakUserUpdater.update(updatedUser);
    } catch (Exception e) {
      throw UserUpdateException.of(updatedUser.getUsername(), e);
    }
  }
}
