package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.user.UsernameUniquenessValidator;
import com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
class KeycloakUsernameUniquenessValidator implements UsernameUniquenessValidator {

  private final UsersResource userResource;

  @Override
  public Optional<UsernameNotUniqueError> validate(String username) {
    log.info(
        LogMarkers.USER_MANAGEMENT, "Checking if username is unique in Keycloak. {}", username);

    if (isUnique(username))
      return Optional.empty();

    return Optional.of(new UsernameNotUniqueError(username));
  }

  private boolean isUnique(String username) {
    return userResource
        .search(username)
        .stream()
        .map(UserRepresentation::getUsername)
        .noneMatch(name -> name.equals(username));
  }
}
