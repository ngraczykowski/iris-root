package com.silenteight.sens.webapp.backend.audit.report;

import com.silenteight.sens.webapp.backend.report.DigitsOnlyDateFormater;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditReportConfiguration {

  @Bean
  AuditReportGenerator auditReportGenerator() {
    return new AuditReportGenerator(DefaultTimeSource.INSTANCE, DigitsOnlyDateFormater.INSTANCE);
  }
}
