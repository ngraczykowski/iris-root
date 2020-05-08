package com.silenteight.sens.webapp.user.password.reset;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResetPasswordConfiguration {

  @Bean
  ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase(
      TemporaryPasswordGenerator temporaryPasswordGenerator,
      UserCredentialsRepository userCredentialsRepository,
      AuditTracer auditTracer) {

    return new ResetInternalUserPasswordUseCase(
        userCredentialsRepository, temporaryPasswordGenerator, auditTracer);
  }
}
