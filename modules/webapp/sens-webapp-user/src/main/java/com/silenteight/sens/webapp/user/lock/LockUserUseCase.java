package com.silenteight.sens.webapp.user.lock;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class LockUserUseCase {

  private final UserLocker userLocker;

  public void apply(LockUserCommand command) {
    userLocker.lock(command.getUsername());
  }

  @Value
  @Builder
  public static class LockUserCommand {

    @NonNull
    private final String username;
  }
}
