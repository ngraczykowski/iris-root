package com.silenteight.sens.webapp.audit.slf4j;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Slf4jBasedAuditLogConfiguration {

  @Bean
  AuditLog slf4jBasedAuditLog() {
    return new Slf4jBasedAuditLog();
  }
}
