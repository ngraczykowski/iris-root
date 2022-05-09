package com.silenteight.sep.usermanagement.keycloak.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsResetter;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.USER_ORIGIN;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
class KeycloakResettableUserCredentials implements UserCredentialsResetter {

  private final UserResource userResource;

  @Override
  public void reset(TemporaryPassword temporaryPassword) {
    log.info("Resetting password");

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
        .filter(originAttributes -> originAttributes.contains(SENS_ORIGIN))
        .isPresent();
  }
}
