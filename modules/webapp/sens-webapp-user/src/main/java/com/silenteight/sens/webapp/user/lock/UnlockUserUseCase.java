package com.silenteight.sens.webapp.user.lock;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class UnlockUserUseCase {

  private final UserLocker userLocker;

  public void apply(UnlockUserCommand command) {
    userLocker.unlock(command.getUsername());
  }

  @Value
  @Builder
  public static class UnlockUserCommand {

    @NonNull
    private final String username;
  }
}
