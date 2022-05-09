package com.silenteight.sep.usermanagement.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers.USER_MANAGEMENT;
import static com.silenteight.sep.usermanagement.keycloak.update.KeycloakUserLocker.unlock;
import static java.lang.Boolean.FALSE;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserUpdater {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;
  @NonNull
  private final KeycloakUserRoleAssigner roleAssigner;
  @NonNull
  private final TimeSource timeSource;

  public void update(UpdateUserCommand updatedUser) {
    log.info(USER_MANAGEMENT, "Updating user. updatedUser={}", updatedUser);

    UserResource userResource = keycloakUserRetriever.retrieve(updatedUser.getUsername());
    UserRepresentation userRepresentation = userResource.toRepresentation();

    if (updatedUser.getDisplayName() != null)
      userRepresentation.setFirstName(updatedUser.getDisplayName());

    if (updatedUser.getRoles() != null)
      assignRoles(userRepresentation.getId(), updatedUser.getRoles());

    if (updatedUser.getLocked() != null) {
      if (updatedUser.getLocked())
        lock(userRepresentation);
      else
        unlock(userRepresentation);
    }

    userResource.update(userRepresentation);
  }

  private void assignRoles(String userId, UserRoles roles) {
    roleAssigner.assignRoles(KeycloakUserId.of(userId), roles);
  }

  private void lock(UserRepresentation userRepresentation) {
    userRepresentation.setEnabled(FALSE);
    userRepresentation.singleAttribute(LOCKED_AT, lockedAtTime());
  }

  private String lockedAtTime() {
    return timeSource.offsetDateTime().toString();
  }
}
