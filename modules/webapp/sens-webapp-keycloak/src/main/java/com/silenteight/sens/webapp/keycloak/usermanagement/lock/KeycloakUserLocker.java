package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.time.TimeSource;
import com.silenteight.sens.webapp.keycloak.usermanagement.retrieval.KeycloakUserRetriever;
import com.silenteight.sens.webapp.user.lock.UserLocker;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.DELETED_AT;
import static java.lang.Boolean.FALSE;

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
    userRepresentation.singleAttribute(DELETED_AT, deletedAtTime());
    userResource.update(userRepresentation);
  }

  private String deletedAtTime() {
    return timeSource.offsetDateTime().toString();
  }
}
