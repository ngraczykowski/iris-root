package com.silenteight.sens.webapp.user.roles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.RolesQuery;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;

@RequiredArgsConstructor
public class ListRolesUseCase {

  @NonNull
  private final RolesQuery rolesQuery;
  @NonNull
  private final String rolesScope;

  public RolesDto apply() {
    return rolesQuery.list(rolesScope);
  }
}
