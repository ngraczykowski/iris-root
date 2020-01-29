package com.silenteight.sens.webapp.backend.users;

import com.silenteight.sens.webapp.backend.users.rest.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {

  Page<UserDto> list(Pageable pageable);
}
