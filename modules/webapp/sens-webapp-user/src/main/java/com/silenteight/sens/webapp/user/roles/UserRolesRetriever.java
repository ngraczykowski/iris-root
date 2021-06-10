package com.silenteight.sens.webapp.user.roles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.UserRoles;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import static java.util.Set.of;

@RequiredArgsConstructor
public class UserRolesRetriever {

  @NonNull
  private final UserQuery userQuery;
  @NonNull
  private final String rolesScope;
  @NonNull
  private final String countryGroupsScope;

  public UserRoles rolesOf(String username) {
    return userQuery
        .find(username, of(rolesScope, countryGroupsScope))
        .map(UserDto::getRoles)
        .orElseThrow(UserNotFoundException::new);
  }
}
