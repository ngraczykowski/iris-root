package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;


import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.util.List;
import java.util.Set;

public interface RolesProvider {

  @NonNull
  UserRoles getForUserId(String userId, Set<String> roleClientIds);

  boolean hasRoleInRealm(String userId, List<String> roles);
}
