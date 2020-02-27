package com.silenteight.sens.webapp.user.lock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserLockUseCaseConfiguration {

  @Bean
  LockUserUseCase lockUserUseCase(UserLocker userLocker) {
    return new LockUserUseCase(userLocker);
  }

  @Bean
  UnlockUserUseCase unlockUserUseCase(UserLocker userLocker) {
    return new UnlockUserUseCase(userLocker);
  }
}
