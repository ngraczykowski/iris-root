package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CircuitBreakerConfiguration {

  @Bean
  ArchiveDiscrepanciesUseCase archiveDiscrepanciesUseCase(
      CircuitBreakerMessageGateway gateway, AuditTracer auditTracer) {

    return new ArchiveDiscrepanciesUseCase(gateway, auditTracer);
  }
}
