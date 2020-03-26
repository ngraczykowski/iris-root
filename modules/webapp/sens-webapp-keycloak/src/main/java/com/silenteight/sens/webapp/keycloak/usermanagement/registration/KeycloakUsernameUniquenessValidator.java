package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator;

import io.vavr.control.Option;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@RequiredArgsConstructor
@Slf4j
class KeycloakUsernameUniquenessValidator implements UsernameUniquenessValidator {

  private final UsersResource userResource;

  @Override
  public Option<UsernameNotUniqueError> validate(String username) {
    log.debug(USER_MANAGEMENT, "Checking if username is unique in Keycloak. {}", username);

    if (isUnique(username))
      return none();

    return of(new UsernameNotUniqueError(username));
  }

  private boolean isUnique(String username) {
    return userResource
        .search(username)
        .stream()
        .map(UserRepresentation::getUsername)
        .noneMatch(name -> name.equals(username));
  }
}
