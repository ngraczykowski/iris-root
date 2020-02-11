package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.registration.domain.UsernameUniquenessValidator;

import io.vavr.control.Option;
import org.keycloak.admin.client.resource.UsersResource;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@RequiredArgsConstructor
@Slf4j
class KeycloakUsernameUniquenessValidator implements UsernameUniquenessValidator {

  private final UsersResource userResource;

  @Override
  public Option<UsernameNotUnique> validate(String username) {
    log.debug("Checking if username is unique in Keycloak. {}", username);

    if (userResource.search(username).isEmpty())
      return none();

    return of(new UsernameNotUnique(username));
  }
}
