package com.silenteight.sep.auth.authorization;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
class StaticPermissionsByRoleProvider implements PermissionsByRoleProvider {

  @NotNull
  private final Map<String, Set<String>> permissionsByRole;

  @Override
  public Map<String, Set<String>> provide() {
    return permissionsByRole;
  }
}
