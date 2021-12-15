package com.silenteight.sens.webapp.audit.log;

import lombok.NonNull;

import com.silenteight.sens.webapp.audit.domain.AuditLogService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditLoggingConfiguration {

  @Bean
  AuditLogUseCase auditLogUseCase(@NonNull AuditLogService auditLogService) {
    return new AuditLogUseCase(auditLogService);
  }
}
