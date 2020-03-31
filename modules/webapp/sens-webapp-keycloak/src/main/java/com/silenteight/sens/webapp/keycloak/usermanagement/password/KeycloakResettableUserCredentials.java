package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.password.reset.ResettableUserCredentials;
import com.silenteight.sens.webapp.user.password.reset.TemporaryPassword;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class KeycloakResettableUserCredentials implements ResettableUserCredentials {

  private final UserResource userResource;

  @Override
  public void reset(TemporaryPassword temporaryPassword) {
    log.info(USER_MANAGEMENT, "Resetting password");

    userResource.resetPassword(createCredentials(temporaryPassword));
  }

  private static CredentialRepresentation createCredentials(TemporaryPassword temporaryPassword) {
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setType("password");
    credentialRepresentation.setValue(temporaryPassword.getPassword());
    credentialRepresentation.setTemporary(true);

    return credentialRepresentation;
  }

  @Override
  public boolean ownerIsInternal() {
    return ofNullable(userResource.toRepresentation().getAttributes())
        .flatMap(attributes -> ofNullable(attributes.get(USER_ORIGIN)))
        .filter(originAttributes -> originAttributes.contains(SENS.toString()))
        .isPresent();
  }
}
