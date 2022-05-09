package com.silenteight.sep.usermanagement.api.user;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.user.dto.CreateUserCommand;

public interface UserCreator {

  void create(@NonNull CreateUserCommand command);
}
