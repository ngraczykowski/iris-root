package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sens.webapp.user.registration.RegisteredUserRepository;
import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Slf4j
@RequiredArgsConstructor
class KeycloakRegisteredUserRepository implements RegisteredUserRepository {

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;
  private final AuditTracer auditTracer;

  @Override
  public void save(CompletedUserRegistration userRegistration) {
    log.info(USER_MANAGEMENT, "Registering User. userRegistration={}", userRegistration);
    UserRepresentation userRepresentation = toUserRepresentation(userRegistration);
    KeycloakUserId newlyCreatedUserId = keycloakUserCreator.create(userRepresentation);

    auditTracer.save(
        new UserCreatedEvent(
            userRegistration.getUsername(),
            CompletedUserRegistration.class.getName(),
            userRegistration.toCompletedUserRegistrationEvent()));

    roleAssigner.assignRoles(newlyCreatedUserId, userRegistration.getRoles());

    auditTracer.save(
        new RolesAssignedEvent(
            userRegistration.getUsername(),
            CompletedUserRegistration.class.getName(),
            userRegistration.getRoles()));
  }

  private static UserRepresentation toUserRepresentation(CompletedUserRegistration registration) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(registration.getUsername());
    userRepresentation.setEnabled(TRUE);
    userRepresentation.setCreatedTimestamp(registration.getRegistrationDate().toEpochSecond());
    userRepresentation.setFirstName(registration.getDisplayName());
    userRepresentation.singleAttribute(USER_ORIGIN, registration.getOrigin());

    registration
        .getCredentials()
        .ifPresent(it -> userRepresentation.setCredentials(
            singletonList(createPasswordCredential(it))));

    return userRepresentation;
  }

  private static CredentialRepresentation createPasswordCredential(Credentials credentials) {
    CredentialRepresentation passwordCredential = new CredentialRepresentation();
    passwordCredential.setValue(credentials.getPassword());
    passwordCredential.setType(PASSWORD);
    passwordCredential.setTemporary(TRUE);

    return passwordCredential;
  }
}
