package com.silenteight.sep.usermanagement.keycloak.assignrole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserRoleAssigner {

  @NotNull
  private final UsersResource usersResource;
  @NotNull
  private final RolesResource rolesResource;

  public void assignRoles(KeycloakUserId userId, Set<String> roles) {
    log.info(
        LogMarkers.USER_MANAGEMENT, "Assigning roles to user. userId={}, roles={}", userId, roles);

    UserResource user = usersResource.get(userId.getUserId());
    RoleScopeResource userRoles = user.roles().realmLevel();

    userRoles.remove(mapToRepresentation(getRolesToRemove(roles, userRoles)));
    userRoles.add(mapToRepresentation(roles));
  }

  @NotNull
  private static List<String> getRolesToRemove(Set<String> roles, RoleScopeResource userRoles) {
    return userRoles
        .listAll()
        .stream()
        .map(RoleRepresentation::getName)
        .filter(role -> !roles.contains(role))
        .collect(toList());
  }

  @NotNull
  private List<RoleRepresentation> mapToRepresentation(Collection<String> roles) {
    return roles
        .stream()
        .map(rolesResource::get)
        .map(RoleResource::toRepresentation)
        .collect(toList());
  }
}
