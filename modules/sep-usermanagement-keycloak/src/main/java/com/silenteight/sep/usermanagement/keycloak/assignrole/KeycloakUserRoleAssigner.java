package com.silenteight.sep.usermanagement.keycloak.assignrole;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.UserRoles;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserId;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserRoleAssigner {

  @NotNull
  private final UsersResource usersResource;
  @NotNull
  private final ClientQuery clientQuery;
  @NonNull
  private final ClientsResource clientsResource;

  public void assignRoles(KeycloakUserId userId, UserRoles roles) {
    log.info(USER_MANAGEMENT, "Assigning roles to user. userId={}, roles={}", userId, roles);
    roles
        .getScopes()
        .forEach(clientId -> assignRoles(userId, clientId, roles.getRoles(clientId)));
  }

  private void assignRoles(KeycloakUserId userId, String roleClientId, Set<String> roles) {
    ClientRepresentation client = clientQuery.getByClientId(roleClientId);
    UserResource user = usersResource.get(userId.getUserId());
    RoleScopeResource userRoles = user.roles().clientLevel(client.getId());

    userRoles.remove(getRolesToRemove(roles, userRoles));
    userRoles.add(getRolesToAdd(roles, client.getId()));
  }

  @NotNull
  private List<RoleRepresentation> getRolesToRemove(
      Collection<String> roles, RoleScopeResource userRoles) {

    return userRoles
        .listAll()
        .stream()
        .filter(role -> !roles.contains(role.getName()))
        .collect(toList());
  }

  @NotNull
  private List<RoleRepresentation> getRolesToAdd(Collection<String> roles, String id) {
    return clientsResource
        .get(id)
        .roles()
        .list()
        .stream()
        .filter(role -> roles.contains(role.getName()))
        .collect(toList());
  }
}
