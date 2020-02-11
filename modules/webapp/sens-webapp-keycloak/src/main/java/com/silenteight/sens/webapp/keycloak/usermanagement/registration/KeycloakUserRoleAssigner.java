package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class KeycloakUserRoleAssigner {

  private final UsersResource usersResource;
  private final RolesResource rolesResource;

  void assignRoles(KeycloakUserId userId, Set<String> roles) {
    UserResource user = this.usersResource.get(userId.getUserId());

    RoleScopeResource userRoles = user.roles().realmLevel();

    List<RoleRepresentation> keycloakFetchedRoles = roles.stream()
        .map(rolesResource::get)
        .map(RoleResource::toRepresentation)
        .collect(toList());

    userRoles.add(keycloakFetchedRoles);
  }
}
