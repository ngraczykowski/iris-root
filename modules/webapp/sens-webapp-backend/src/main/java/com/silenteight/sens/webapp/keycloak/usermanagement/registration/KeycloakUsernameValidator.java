package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameValidator;

import org.keycloak.admin.client.resource.UsersResource;

@RequiredArgsConstructor
@Slf4j
class KeycloakUsernameValidator implements UsernameValidator {

  private final UsersResource userResource;

  @Override
  public boolean isUnique(String username) {
    log.debug("Checking if username is unique in Keycloak. {}", username);
    return userResource.search(username).isEmpty();
  }
}
