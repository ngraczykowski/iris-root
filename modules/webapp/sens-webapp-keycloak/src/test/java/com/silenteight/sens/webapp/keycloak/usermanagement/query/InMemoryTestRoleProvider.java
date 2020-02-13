package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.NonNull;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

class InMemoryTestRoleProvider implements RolesProvider {

  private final Map<String, List<String>> repo = new HashMap<>();

  void add(String userId, @NonNull List<String> roles) {
    repo.put(userId, roles);
  }

  @Override
  public @NonNull List<String> getForUserId(String userId) {
    return repo.getOrDefault(userId, emptyList());
  }
}
