package com.silenteight.sep.auth.authorization;

import lombok.NonNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class RolePermissionMethodSecurityExpressionRoot extends BaseSecurityExpressionRoot {

  @NotNull
  private final Map<String, Set<String>> permissionsByMethod;
  @NonNull
  private final PermissionsByRoleProvider permissionsByRoleProvider;

  public RolePermissionMethodSecurityExpressionRoot(
      Authentication authentication,
      Map<String, Set<String>> permissionsByMethod,
      PermissionsByRoleProvider permissionsByRoleProvider) {

    super(authentication);
    this.permissionsByMethod = permissionsByMethod;
    this.permissionsByRoleProvider = permissionsByRoleProvider;
  }

  @Override
  public boolean isAuthorized(@NonNull String method) {
    return ofNullable(permissionsByMethod.get(method))
        .map(this::isAuthorized)
        .orElse(false);
  }

  private boolean isAuthorized(@NonNull Set<String> methodPermissions) {
    return getUserPermissions()
        .stream()
        .anyMatch(methodPermissions::contains);
  }

  private List<String> getUserPermissions() {
    return mapRolesToPermissions(getUserRoles());
  }

  private List<String> getUserRoles() {
    return getAuthentication()
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(toList());
  }

  private List<String> mapRolesToPermissions(@NonNull List<String> roles) {
    Map<String, Set<String>> permissionsByRole = permissionsByRoleProvider.provide();

    return roles
        .stream()
        .map(permissionsByRole::get)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .collect(toList());
  }
}
