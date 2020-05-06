package com.silenteight.sens.webapp.audit.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditTracerConfiguration {

  @Bean
  Slf4jAuditTracer slf4jAuditTracer() {
    return new Slf4jAuditTracer();
  }
}
