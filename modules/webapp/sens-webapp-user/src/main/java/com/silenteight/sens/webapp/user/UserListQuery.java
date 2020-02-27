package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.user.dto.UserDto;

import java.util.Collection;

public interface UserListQuery {

  Collection<UserDto> listEnabled();

  Collection<UserDto> listAll();
}
