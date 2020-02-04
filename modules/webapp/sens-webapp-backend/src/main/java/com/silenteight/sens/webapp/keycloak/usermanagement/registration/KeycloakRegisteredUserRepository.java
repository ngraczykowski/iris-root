package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.user.registration.RegisteredUserRepository;
import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.backend.user.registration.domain.NewUserDetails.Credentials;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static java.util.List.of;

@RequiredArgsConstructor
@Slf4j
class KeycloakRegisteredUserRepository implements RegisteredUserRepository {

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;

  @Override
  public void save(CompletedUserRegistration userRegistration) {
    UserRepresentation userRepresentation = toUserRepresentation(userRegistration);

    KeycloakUserId newlyCreatedUserId = keycloakUserCreator.createUser(userRepresentation);

    roleAssigner.assignRoles(newlyCreatedUserId, userRegistration.getRoles());
  }

  private static UserRepresentation toUserRepresentation(CompletedUserRegistration registration) {
    UserRepresentation userRepresentation = new UserRepresentation();

    Credentials credentials = registration.getCredentials();
    String username = registration.getUsername();

    userRepresentation.setUsername(username);
    userRepresentation.setEnabled(Boolean.TRUE);

    CredentialRepresentation password = new CredentialRepresentation();
    password.setValue(credentials.getPassword());
    password.setType(CredentialRepresentation.PASSWORD);
    password.setTemporary(Boolean.TRUE);

    userRepresentation.setCredentials(of(password));
    userRepresentation.setCreatedTimestamp(registration.getRegistrationDate().toEpochSecond());
    userRepresentation.setFirstName(registration.getDisplayName());

    return userRepresentation;
  }
}
