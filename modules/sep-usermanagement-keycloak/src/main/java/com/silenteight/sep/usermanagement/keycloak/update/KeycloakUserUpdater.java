package com.silenteight.sep.usermanagement.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserUpdater {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;
  @NonNull
  private final KeycloakUserRoleAssigner roleAssigner;

  public void update(UpdatedUser updatedUser) {
    log.info(LogMarkers.USER_MANAGEMENT, "Updating user. updatedUser={}", updatedUser);

    UserResource userResource = keycloakUserRetriever.retrieve(updatedUser.getUsername());
    UserRepresentation userRepresentation = userResource.toRepresentation();
    if (updatedUser.getDisplayName() != null) {
      userRepresentation.setFirstName(updatedUser.getDisplayName());
    }
    if (updatedUser.getRoles() != null) {
      assignRoles(userRepresentation.getId(), updatedUser.getRoles());
    }
    userResource.update(userRepresentation);
  }

  public void assignRoles(String id, Set<String> roles) {
    roleAssigner.assignRoles(KeycloakUserId.of(id), roles);
  }
}
