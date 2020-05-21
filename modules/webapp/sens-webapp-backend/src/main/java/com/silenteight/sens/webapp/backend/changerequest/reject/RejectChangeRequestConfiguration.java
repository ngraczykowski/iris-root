package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RejectChangeRequestConfiguration {

  @Bean
  RejectChangeRequestUseCase rejectChangeRequestUseCase(AuditTracer auditTracer) {
    return new RejectChangeRequestUseCase(auditTracer);
  }
}
