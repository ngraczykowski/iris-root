package com.silenteight.sens.webapp.audit.trace;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditTracerConfiguration {

  @Bean
  DatabaseAuditTracer databaseAuditTracer(AuditingLogger auditingLogger) {
    return new DatabaseAuditTracer(auditingLogger);
  }
}
