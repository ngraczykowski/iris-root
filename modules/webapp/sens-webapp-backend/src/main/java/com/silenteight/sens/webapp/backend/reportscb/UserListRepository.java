package com.silenteight.sens.webapp.backend.reportscb;

import com.silenteight.sens.webapp.user.dto.UserDto;

import java.util.Collection;

public interface UserListRepository {

  Collection<UserDto> list();
}
