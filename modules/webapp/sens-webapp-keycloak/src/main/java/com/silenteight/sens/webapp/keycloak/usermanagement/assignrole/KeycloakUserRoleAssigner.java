package com.silenteight.sens.webapp.keycloak.usermanagement.assignrole;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakUserRoleAssigner {

  @NotNull
  private final UsersResource usersResource;
  @NotNull
  private final RolesResource rolesResource;
  @NotNull
  private final AuditLog auditLog;

  public void assignRoles(KeycloakUserId userId, Set<String> roles) {
    auditLog.logInfo(
        USER_MANAGEMENT, "Assigning roles to user. userId={}, roles={}", userId, roles);

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
