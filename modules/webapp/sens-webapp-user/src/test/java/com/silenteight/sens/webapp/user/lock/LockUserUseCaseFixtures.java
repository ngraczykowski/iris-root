package com.silenteight.sens.webapp.user.lock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.lock.LockUserUseCase.LockUserCommand;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class LockUserUseCaseFixtures {

  static final LockUserCommand LOCK_COMMAND = LockUserCommand
      .builder()
      .username("smith")
      .build();
}
