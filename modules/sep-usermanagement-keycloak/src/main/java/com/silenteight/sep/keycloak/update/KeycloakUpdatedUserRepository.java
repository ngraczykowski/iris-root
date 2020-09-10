package com.silenteight.sep.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.sillenteight.sep.usermanagement.api.UpdatedUser;
import com.sillenteight.sep.usermanagement.api.UpdatedUserRepository;

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
