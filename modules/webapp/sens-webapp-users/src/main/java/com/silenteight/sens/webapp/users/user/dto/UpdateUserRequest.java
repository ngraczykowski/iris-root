package com.silenteight.sens.webapp.users.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.UserRoleInUseChecker;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateUserRequest {

  private final long userId;
  private final String displayName;
  private final String password;
  private final Boolean superUser;
  private final Boolean active;
  private final List<Role> roles;

  public long getUserId() {
    return userId;
  }

  public Optional<String> getDisplayName() {
    return ofNullable(displayName);
  }

  public Optional<String> getPassword() {
    return ofNullable(password);
  }

  public Optional<Boolean> getSuperUser() {
    return ofNullable(superUser);
  }

  public Optional<Boolean> getActive() {
    return ofNullable(active);
  }

  public Optional<List<Role>> getRoles() {
    return ofNullable(roles);
  }

  public static UpdateUserRequest changePassword(long userId, String password) {
    return UpdateUserRequest.builder().userId(userId).password(password).build();
  }

  public void checkRoles(UserRoleInUseChecker roleInUseChecker) {
    getRoles().ifPresent(newRoles -> doCheckRole(roleInUseChecker, newRoles));
  }

  private void doCheckRole(UserRoleInUseChecker roleInUseChecker, List<Role> newRoles) {
    Optional<Boolean> isSuperUser = getSuperUser();
    if (isSuperUser.isPresent())
      roleInUseChecker.canSetRoles(userId, newRoles, isSuperUser.get());
    else
      roleInUseChecker.canSetRoles(userId, newRoles);
  }
}
