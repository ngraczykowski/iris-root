package com.silenteight.sens.webapp.keycloak.usermanagement.password;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.password.reset.ResettableUserCredentials;
import com.silenteight.sens.webapp.user.password.reset.TemporaryPassword;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.user.domain.SensOrigin.SENS_ORIGIN;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class KeycloakResettableUserCredentials implements ResettableUserCredentials {

  private final UserResource userResource;

  private final AuditLog auditLog;

  @Override
  public void reset(TemporaryPassword temporaryPassword) {
    auditLog.logInfo(USER_MANAGEMENT, "Resetting password");

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
