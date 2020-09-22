package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.UserLocker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserLockUseCaseConfiguration {

  @Bean
  LockUserUseCase lockUserUseCase(UserLocker userLocker, AuditTracer auditTracer) {
    return new LockUserUseCase(userLocker, auditTracer);
  }

  @Bean
  UnlockUserUseCase unlockUserUseCase(UserLocker userLocker, AuditTracer auditTracer) {
    return new UnlockUserUseCase(userLocker, auditTracer);
  }
}
