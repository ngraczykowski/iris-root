package com.silenteight.sep.usermanagement.api.user;

import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserQuery {

  List<UserDto> listAll(Set<String> roleScopes);

  Optional<UserDto> find(String username, Set<String> roleScopes);

  List<UserDto> listAll(String roleName, String roleScope);
}
