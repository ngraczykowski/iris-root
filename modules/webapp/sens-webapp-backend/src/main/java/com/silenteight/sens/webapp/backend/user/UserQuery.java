package com.silenteight.sens.webapp.backend.user;

import com.silenteight.sens.webapp.backend.user.rest.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {

  Page<UserDto> list(Pageable pageable);
}
