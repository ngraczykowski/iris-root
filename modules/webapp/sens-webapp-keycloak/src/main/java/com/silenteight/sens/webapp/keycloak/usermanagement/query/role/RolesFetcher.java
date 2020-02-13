package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class RolesFetcher {

  @NonNull
  private final RolesResource rolesResource;

  @NonNull
  Map<String, @NonNull List<String>> fetch() {
    return rolesResource
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .map(this::getRoleWithUsers)
        .flatMap(this::getUserWithRole)
        .collect(groupingBy(UserWithRole::getUserId, mapping(UserWithRole::getRole, toList())));
  }

  @NotNull
  private Stream<UserWithRole> getUserWithRole(RoleWithUsers it) {
    return it.getUsers().stream().map(user -> new UserWithRole(user, it.role));
  }

  @NonNull
  private RoleWithUsers getRoleWithUsers(String roleName) {
    return new RoleWithUsers(roleName, rolesResource.get(roleName).getRoleUserMembers());
  }

  @Value
  private static class RoleWithUsers {
    String role;
    Set<UserRepresentation> users;
  }

  @Value
  private static class UserWithRole {
    private final UserRepresentation user;
    private final String role;

    String getUserId() {
      return user.getId();
    }
  }
}
