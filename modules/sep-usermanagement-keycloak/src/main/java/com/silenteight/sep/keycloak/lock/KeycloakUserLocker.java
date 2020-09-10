package com.silenteight.sep.keycloak.lock;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.keycloak.KeycloakUserAttributeNames;
import com.silenteight.sep.keycloak.retrieval.KeycloakUserRetriever;

import com.sillenteight.sep.usermanagement.api.UserLocker;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class KeycloakUserLocker implements UserLocker {

  @NonNull
  private final KeycloakUserRetriever keycloakUserRetriever;
  @NonNull
  private final TimeSource timeSource;

  @Override
  public void lock(String username) {
    log.debug("Locking user. username={}", username);

    UserResource userResource = keycloakUserRetriever.retrieve(username);
    UserRepresentation userRepresentation = userResource.toRepresentation();
    userRepresentation.setEnabled(FALSE);
    userRepresentation.singleAttribute(KeycloakUserAttributeNames.DELETED_AT, deletedAtTime());
    userResource.update(userRepresentation);
  }

  private String deletedAtTime() {
    return timeSource.offsetDateTime().toString();
  }

  @Override
  public void unlock(String username) {
    log.debug("Unlocking user. username={}", username);

    UserResource userResource = keycloakUserRetriever.retrieve(username);
    UserRepresentation userRepresentation = userResource.toRepresentation();
    userRepresentation.setEnabled(TRUE);
    ofNullable(userRepresentation
        .getAttributes())
        .ifPresent(attributes -> attributes.remove(KeycloakUserAttributeNames.DELETED_AT));
    userResource.update(userRepresentation);
  }
}
