package com.silenteight.sens.webapp.user.roles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.UserRoles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserRolesMapper {

  public static Map<String, Set<String>> map(@NonNull UserRoles roles) {
    Map<String, Set<String>> mapped = new HashMap<>();
    roles
        .getScopes()
        .forEach(scope -> mapped.put(scope, roles.getRoles(scope)));

    return mapped;
  }
}
