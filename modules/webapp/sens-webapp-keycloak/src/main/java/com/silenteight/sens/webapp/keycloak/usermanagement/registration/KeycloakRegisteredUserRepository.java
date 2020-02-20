package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;
import com.silenteight.sens.webapp.keycloak.usermanagement.assignrole.KeycloakUserRoleAssigner;
import com.silenteight.sens.webapp.user.registration.RegisteredUserRepository;
import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@RequiredArgsConstructor
@Slf4j
class KeycloakRegisteredUserRepository implements RegisteredUserRepository {

  private static final String SOURCE_ATTRIBUTE_NAME = "source";

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;

  @Override
  public void save(CompletedUserRegistration userRegistration) {
    UserRepresentation userRepresentation = toUserRepresentation(userRegistration);

    KeycloakUserId newlyCreatedUserId = keycloakUserCreator.create(userRepresentation);

    roleAssigner.assignRoles(newlyCreatedUserId, userRegistration.getRoles());
  }

  private static UserRepresentation toUserRepresentation(CompletedUserRegistration registration) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(registration.getUsername());
    userRepresentation.setEnabled(TRUE);
    userRepresentation.setCreatedTimestamp(registration.getRegistrationDate().toEpochSecond());
    userRepresentation.setFirstName(registration.getDisplayName());
    userRepresentation.singleAttribute(SOURCE_ATTRIBUTE_NAME, registration.getSourceName());

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
