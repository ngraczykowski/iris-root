package com.silenteight.sens.webapp.user;

import com.silenteight.sens.webapp.user.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuery {

  Page<UserDto> listEnabled(Pageable pageable);
}
