package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.user.UserCreator;
import com.silenteight.sep.usermanagement.api.user.dto.CreateUserCommand;
import com.silenteight.sep.usermanagement.api.user.dto.NewUserDetails.Credentials;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.assignrole.KeycloakUserRoleAssigner;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Slf4j
@RequiredArgsConstructor
class KeycloakRegisteredUserRepository implements UserCreator {

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;

  @Override
  public void create(@NonNull CreateUserCommand command) {
    log.info("Registering User. userRegistration={}", command);
    UserRepresentation userRepresentation = toUserRepresentation(command);
    KeycloakUserId newlyCreatedUserId = keycloakUserCreator.create(userRepresentation);

    roleAssigner.assignRoles(newlyCreatedUserId, command.getRoles());
  }

  private static UserRepresentation toUserRepresentation(CreateUserCommand command) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(command.getUsername());
    userRepresentation.setEnabled(TRUE);
    userRepresentation.setCreatedTimestamp(command.getRegistrationDate().toEpochSecond());
    userRepresentation.setFirstName(command.getDisplayName());
    userRepresentation.singleAttribute(
        KeycloakUserAttributeNames.USER_ORIGIN, command.getOrigin());

    if (command.getCredentials() != null) {
      userRepresentation.setCredentials(
          singletonList(createPasswordCredential(command.getCredentials())));
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
