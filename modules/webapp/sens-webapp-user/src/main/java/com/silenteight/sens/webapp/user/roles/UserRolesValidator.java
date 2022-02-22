package com.silenteight.sens.webapp.user.roles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.role.validate.RoleAssignmentValidator;
import com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCase;

@RequiredArgsConstructor
class UserRolesValidator implements RoleAssignmentValidator {

  @NonNull
  private final ListUsersWithRoleUseCase listUsersWithRoleUseCase;

  @Override
  public boolean isAssigned(@NonNull String roleName) {
    return !listUsersWithRoleUseCase
        .apply(roleName)
        .isEmpty();
  }
}
