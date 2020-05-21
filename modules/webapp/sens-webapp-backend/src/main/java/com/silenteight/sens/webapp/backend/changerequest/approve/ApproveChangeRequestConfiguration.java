package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApproveChangeRequestConfiguration {

  @Bean
  ApproveChangeRequestUseCase approveChangeRequestUseCase(AuditTracer auditTracer) {
    return new ApproveChangeRequestUseCase(auditTracer);
  }
}
