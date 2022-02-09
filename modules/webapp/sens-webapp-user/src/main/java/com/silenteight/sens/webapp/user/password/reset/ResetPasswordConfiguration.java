package com.silenteight.sens.webapp.user.password.reset;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.credentials.TemporaryPasswordGenerator;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResetPasswordConfiguration {

  @Bean
  ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase(
      TemporaryPasswordGenerator temporaryPasswordGenerator,
      UserCredentialsQuery userCredentialsQuery,
      AuditTracer auditTracer) {

    return new ResetInternalUserPasswordUseCase(
        userCredentialsQuery, temporaryPasswordGenerator, auditTracer);
  }
}
