package com.silenteight.sep.usermanagement.keycloak.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.RolesValidator;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
class KeycloakRolesValidator implements RolesValidator {

  @NonNull
  private final ClientsResource clientsResource;

  @Override
  public Optional<RolesDontExistError> validate(@NonNull String scope, @NonNull Set<String> roles) {
    log.debug("Checking if roles exist {} in a scope {}", roles, scope);

    if (roles.isEmpty())
      throw new IllegalArgumentException("You need to provide roles to check.");

    Set<String> availableRoles = clientsResource
        .get(scope)
        .roles()
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .collect(toSet());

    if (availableRoles.containsAll(roles))
      return empty();

    return Optional.of(new RolesDontExistError(roles));
  }
}
