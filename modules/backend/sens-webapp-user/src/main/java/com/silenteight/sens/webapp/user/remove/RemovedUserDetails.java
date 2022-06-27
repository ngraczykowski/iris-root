package com.silenteight.sens.webapp.user.remove;

import lombok.Value;

import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;

import java.util.Collection;

@Value
public class RemovedUserDetails {

  private RemoveUserCommand command;
  private Collection<String> roles;
}
