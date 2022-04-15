package com.silenteight.sens.webapp.role.validate;

import lombok.NonNull;

public interface RoleAssignmentValidator {

  boolean isAssigned(@NonNull String roleName);
}
