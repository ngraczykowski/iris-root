package com.silenteight.sep.keycloak.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.keycloak.KeycloakUserAttributeNames;
import com.silenteight.sep.keycloak.KeycloakUserId;
import com.silenteight.sep.keycloak.assignrole.KeycloakUserRoleAssigner;

import com.sillenteight.sep.usermanagement.api.CompletedUserRegistration;
import com.sillenteight.sep.usermanagement.api.NewUserDetails.Credentials;
import com.sillenteight.sep.usermanagement.api.RegisteredUserRepository;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Slf4j
@RequiredArgsConstructor
class KeycloakRegisteredUserRepository implements RegisteredUserRepository {

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;

  @Override
  public void save(CompletedUserRegistration userRegistration) {
    log.info("Registering User. userRegistration={}", userRegistration);
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
    userRepresentation.singleAttribute(
        KeycloakUserAttributeNames.USER_ORIGIN, registration.getOrigin());

    if (registration.getCredentials() != null) {
      userRepresentation.setCredentials(
          singletonList(createPasswordCredential(registration.getCredentials())));
    }

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
