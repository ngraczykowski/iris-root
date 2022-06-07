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
import org.springframework.beans.factory.annotation.Value;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Slf4j
@RequiredArgsConstructor
class KeycloakRegisteredUserRepository implements UserCreator {

  /**
   * only used when execting e2e tests creates new user with or without temporary password flag this
   * is technical flag which should not be set on production systems thus injected as @Value
   * property
   */
  @Value("${keycloak.create-temporary-password:true}")
  private boolean createTemporaryPassword = true;

  private final KeycloakUserCreator keycloakUserCreator;
  private final KeycloakUserRoleAssigner roleAssigner;

  @Override
  public void create(@NonNull CreateUserCommand command) {
    log.info("Registering User. userRegistration={}", command);
    UserRepresentation userRepresentation = toUserRepresentation(command);
    KeycloakUserId newlyCreatedUserId = keycloakUserCreator.create(userRepresentation);

    roleAssigner.assignRoles(newlyCreatedUserId, command.getRoles());
  }

  private UserRepresentation toUserRepresentation(CreateUserCommand command) {
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

  private CredentialRepresentation createPasswordCredential(Credentials credentials) {
    CredentialRepresentation passwordCredential = new CredentialRepresentation();
    passwordCredential.setValue(credentials.getPassword());
    passwordCredential.setType(PASSWORD);
    passwordCredential.setTemporary(createTemporaryPassword);

    return passwordCredential;
  }
}
