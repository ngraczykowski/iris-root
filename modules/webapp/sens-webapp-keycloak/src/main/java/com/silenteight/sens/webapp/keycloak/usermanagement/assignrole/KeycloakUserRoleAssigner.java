package com.silenteight.sens.webapp.keycloak.usermanagement.assignrole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;

import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserRoleAssigner {

  private final UsersResource usersResource;
  private final RolesResource rolesResource;

  public void assignRoles(KeycloakUserId userId, Set<String> roles) {
    log.debug("Assigning roles to user. userId={}, roles={}", userId, roles);

    UserResource user = usersResource.get(userId.getUserId());
    RoleScopeResource userRoles = user.roles().realmLevel();

    List<RoleRepresentation> keycloakFetchedRoles = roles.stream()
        .map(rolesResource::get)
        .map(RoleResource::toRepresentation)
        .collect(toList());

    userRoles.add(keycloakFetchedRoles);
  }
}
