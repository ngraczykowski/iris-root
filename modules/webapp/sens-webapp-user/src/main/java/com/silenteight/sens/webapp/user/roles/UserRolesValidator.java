package com.silenteight.sens.webapp.user.roles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.role.validate.RoleAssignmentValidator;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

@RequiredArgsConstructor
class UserRolesValidator implements RoleAssignmentValidator {

  @NonNull
  private final UserQuery userQuery;

  @NonNull
  private final String roleScope;

  @Override
  public boolean isAssigned(@NonNull String roleName) {
    return !userQuery
        .listAll(roleName, roleScope)
        .isEmpty();
  }
}
