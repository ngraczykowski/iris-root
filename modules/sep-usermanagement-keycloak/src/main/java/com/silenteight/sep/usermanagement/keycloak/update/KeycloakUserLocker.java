package com.silenteight.sep.usermanagement.keycloak.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.user.UserLocker;
import com.silenteight.sep.usermanagement.keycloak.retrieval.KeycloakUserRetriever;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserLocker implements UserLocker {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;

  @Override
  public void unlock(String username) {
    log.debug("Unlocking user. username={}", username);

    UserResource userResource = keycloakUserRetriever.retrieve(username);
    UserRepresentation userRepresentation = userResource.toRepresentation();
    unlock(userRepresentation);
    userResource.update(userRepresentation);
  }

  static void unlock(UserRepresentation userRepresentation) {
    userRepresentation.setEnabled(TRUE);
    ofNullable(userRepresentation
        .getAttributes())
        .ifPresent(attributes -> attributes.remove(LOCKED_AT));
  }
}
