package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import java.util.List;

@RequiredArgsConstructor
class RealmRoleFilter {

  @NonNull
  private final List<String> roles;
  @NonNull
  private final RolesProvider rolesProvider;

  boolean isConfigured() {
    return !roles.isEmpty();
  }

  boolean visible(String userId) {
    return rolesProvider.hasRoleInRealm(userId, roles);
  }
}
