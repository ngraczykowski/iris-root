package com.silenteight.sens.webapp.user.lock;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.user.UserLocker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserLockUseCaseConfiguration {

  @Bean
  UnlockUserUseCase unlockUserUseCase(UserLocker userLocker, AuditTracer auditTracer) {
    return new UnlockUserUseCase(userLocker, auditTracer);
  }
}
