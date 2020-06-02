package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BulkChangeConfiguration {

  @Bean
  CreateBulkChangeUseCase createBulkChangeUseCase(
      AuditTracer auditTracer, CreateBulkChangeMessageGateway createBulkChangeMessageGateway) {
    return new CreateBulkChangeUseCase(createBulkChangeMessageGateway, auditTracer);
  }
}
