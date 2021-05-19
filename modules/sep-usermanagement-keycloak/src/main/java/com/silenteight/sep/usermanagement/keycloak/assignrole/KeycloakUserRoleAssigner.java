package com.silenteight.sep.usermanagement.keycloak.assignrole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserRoleAssigner {

  @NotNull
  private final UsersResource usersResource;
  @NotNull
  private final RolesResource rolesResource;
  @NotNull
  private final ClientQuery clientQuery;

  public void assignRoles(KeycloakUserId userId, Map<String, Set<String>> roles) {
    log.info(USER_MANAGEMENT, "Assigning roles to user. userId={}, roles={}", userId, roles);
    roles
        .entrySet()
        .forEach(rolesForClientId ->
            assignRoles(userId, rolesForClientId.getKey(), rolesForClientId.getValue()));
  }

  private void assignRoles(KeycloakUserId userId, String roleClientId, Set<String> roles) {
    ClientRepresentation client = clientQuery.getByClientId(roleClientId);
    UserResource user = usersResource.get(userId.getUserId());
    RoleScopeResource userRoles = user.roles().clientLevel(client.getId());

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
