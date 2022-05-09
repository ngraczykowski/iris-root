package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ExtractedUserRoles implements UserRoles {

  @NonNull
  private final Map<String, Set<String>> roles;

  @Override
  public @NonNull Set<String> getRoles(@NonNull String scope) {
    return getRolesForScope(scope);
  }

  @Override
  public @NonNull List<String> getSortedRoles(@NonNull String scope) {
    return getRolesForScope(scope)
        .stream()
        .sorted()
        .collect(toList());
  }

  private Set<String> getRolesForScope(String scope) {
    return roles.getOrDefault(scope, emptySet());
  }

  @Override
  public @NonNull Set<String> getScopes() {
    return roles.keySet();
  }
}
