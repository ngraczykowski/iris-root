package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.UserRoles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;

public class InMemoryTestRoleProvider implements RolesProvider {

  private final Map<String, UserRoles> repository = new HashMap<>();

  public void add(String userId, @NonNull UserRoles roles) {
    repository.put(userId, roles);
  }

  @Override
  public @NonNull UserRoles getForUserId(String userId, Set<String> roleClientIds) {
    return repository.getOrDefault(userId, new ExtractedUserRoles(emptyMap()));
  }
}
