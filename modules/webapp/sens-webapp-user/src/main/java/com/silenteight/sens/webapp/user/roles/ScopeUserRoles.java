package com.silenteight.sens.webapp.user.roles;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.UserRoles;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;

@EqualsAndHashCode
@RequiredArgsConstructor
public class ScopeUserRoles implements UserRoles {

  @NonNull
  private final Map<String, List<String>> roles;

  @Override
  public @NonNull Set<String> getRoles(@NonNull String scope) {
    return new HashSet<>(getRolesForScope(scope));
  }

  @Override
  public @NonNull List<String> getSortedRoles(@NonNull String scope) {
    return getRolesForScope(scope);
  }

  @Override
  public @NonNull Set<String> getScopes() {
    return roles.keySet();
  }

  private List<String> getRolesForScope(String scope) {
    return roles.getOrDefault(scope, emptyList());
  }
}
