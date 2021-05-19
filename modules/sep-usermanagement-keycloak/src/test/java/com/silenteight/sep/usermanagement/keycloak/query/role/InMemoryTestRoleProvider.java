package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class InMemoryTestRoleProvider implements RolesProvider {

  private final Map<String, Map<String, List<String>>> repository = new HashMap<>();

  public void add(String userId, @NonNull Map<String, List<String>> roles) {
    repository.put(userId, roles);
  }

  @Override
  public @NonNull Map<String, List<String>> getForUserId(String userId, Set<String> roleClientIds) {
    Map<String, List<String>> userRoles = repository.getOrDefault(userId, emptyMap());
    Map<String, List<String>> result = new HashMap<>();
    for (String roleClientId: roleClientIds)
      result.put(roleClientId, userRoles.getOrDefault(roleClientId, emptyList()));

    return result;
  }
}
