package com.silenteight.sens.webapp.audit.domain;

import lombok.NonNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class AuditDomainConfiguration {

  @Bean
  AuditLogService auditLogService(@NonNull AuditLogRepository repository) {
    return new AuditLogService(repository);
  }

  @Bean
  AuditLogQuery auditLogQuery(@NonNull AuditLogRepository repository) {
    return new AuditLogQuery(repository);
  }
}
