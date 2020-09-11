package com.silenteight.sep.usermanagement.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;

@RequiredArgsConstructor
class KeycloakUpdatedUserRepository implements UpdatedUserRepository {

  @NonNull
  private final KeycloakUserUpdater keycloakUserUpdater;


  @Override
  public void save(UpdatedUser updatedUser) {
    try {
      keycloakUserUpdater.update(updatedUser);
    } catch (Exception e) {
      throw UserUpdateException.of(updatedUser.getUsername(), e);
    }
  }
}
