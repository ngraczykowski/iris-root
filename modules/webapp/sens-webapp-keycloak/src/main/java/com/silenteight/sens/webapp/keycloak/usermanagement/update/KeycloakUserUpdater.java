package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sens.webapp.user.update.UpdatedUser;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Set;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserUpdater {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;

  @NonNull
  private final KeycloakUserRoleAssigner roleAssigner;

  void update(UpdatedUser updatedUser) {
    log.info(USER_MANAGEMENT, "Updating user. updatedUser={}", updatedUser);

    UserResource userResource = keycloakUserRetriever.retrieve(updatedUser.getUsername());
    UserRepresentation userRepresentation = userResource.toRepresentation();
    updatedUser.getDisplayName().ifPresent(userRepresentation::setFirstName);
    updatedUser.getRoles().ifPresent(roles -> assignRoles(userRepresentation.getId(), roles));
    userResource.update(userRepresentation);
  }

  void assignRoles(String id, Set<String> roles) {
    roleAssigner.assignRoles(KeycloakUserId.of(id), roles);
  }
}
