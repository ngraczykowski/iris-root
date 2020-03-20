package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.password.ResettableUserCredentials;
import com.silenteight.sens.webapp.user.password.TemporaryPassword;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class KeycloakResettableUserCredentials implements ResettableUserCredentials {

  private final UserResource userResource;

  @Override
  public void reset(TemporaryPassword temporaryPassword) {
    userResource.resetPassword(createCredentials(temporaryPassword));
  }

  private static CredentialRepresentation createCredentials(TemporaryPassword temporaryPassword) {
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setType("password");
    credentialRepresentation.setValue(temporaryPassword.getPassword());

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
