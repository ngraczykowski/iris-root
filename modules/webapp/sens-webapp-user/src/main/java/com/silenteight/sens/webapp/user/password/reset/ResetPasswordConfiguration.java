package com.silenteight.sens.webapp.user.password.reset;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResetPasswordConfiguration {

  @Bean
  ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase(
      TemporaryPasswordGenerator temporaryPasswordGenerator,
      UserCredentialsRepository userCredentialsRepository,
      AuditLog auditLog) {
    return new ResetInternalUserPasswordUseCase(
        userCredentialsRepository, temporaryPasswordGenerator, auditLog);
  }
}
