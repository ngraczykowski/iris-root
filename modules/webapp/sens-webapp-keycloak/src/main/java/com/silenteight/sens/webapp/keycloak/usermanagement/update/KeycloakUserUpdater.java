package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sens.webapp.user.update.UpdatedUser;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserUpdater {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;

  void update(UpdatedUser updatedUser) {
    log.debug("Updating user. updatedUser={}", updatedUser);

    UserResource userResource = keycloakUserRetriever.retrieve(updatedUser.getUsername());
    UserRepresentation userRepresentation = userResource.toRepresentation();
    updatedUser.getDisplayName().ifPresent(userRepresentation::setFirstName);
    userResource.update(userRepresentation);
  }
}
