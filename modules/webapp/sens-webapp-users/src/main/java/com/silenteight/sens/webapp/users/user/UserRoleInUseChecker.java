package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.kernel.security.authority.Role;

import java.util.List;

public interface UserRoleInUseChecker {

  void canSetRoles(Long userId, List<Role> roles);

  void canSetRoles(Long userId, List<Role> roles, boolean isSuperuser);
}
