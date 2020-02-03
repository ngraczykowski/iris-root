package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.user.registration.domain.RolesValidator;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
class KeycloakRolesValidator implements RolesValidator {

  private final RolesResource keycloak;

  @Override
  public boolean rolesExist(@NonNull Set<String> roles) {
    log.debug("Checking if roles exist. {}", roles);
    
    if (roles.isEmpty())
      throw new IllegalArgumentException("You need to provide roles to check.");

    Set<String> availableRoles = keycloak.list().stream()
        .map(RoleRepresentation::getName)
        .collect(toSet());

    return availableRoles.containsAll(roles);
  }
}
