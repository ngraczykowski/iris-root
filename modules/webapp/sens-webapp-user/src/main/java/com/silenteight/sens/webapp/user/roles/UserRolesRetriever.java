package com.silenteight.sens.webapp.user.roles;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
public class UserRolesRetriever {

  @NonNull
  private final UserQuery userQuery;

  public List<String> rolesOf(String username) {
    return userQuery
        .find(username)
        .map(UserDto::getRoles)
        .orElseThrow(UserNotFoundException::new);
  }
}
