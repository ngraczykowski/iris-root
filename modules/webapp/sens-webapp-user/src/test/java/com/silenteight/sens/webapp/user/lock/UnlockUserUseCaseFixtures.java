package com.silenteight.sens.webapp.user.lock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase.UnlockUserCommand;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UnlockUserUseCaseFixtures {

  static final UnlockUserCommand UNLOCK_COMMAND = UnlockUserCommand
      .builder()
      .username("jsmith")
      .build();
}
