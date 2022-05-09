package com.silenteight.sep.usermanagement.api.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
class TestUserRoles implements UserRoles {

  @NonNull
  private final String scope;
  @NonNull
  private final List<String> roles;

  @Override
  public @NonNull Set<String> getRoles(@NonNull String scope) {
    return new HashSet<>(roles);
  }

  @Override
  public @NonNull List<String> getSortedRoles(@NonNull String scope) {
    return new ArrayList<>(roles);
  }

  @Override
  public @NonNull Set<String> getScopes() {
    return Set.of(scope);
  }
}
