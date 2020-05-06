package com.silenteight.sens.webapp.audit.report;

import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.common.time.DigitsOnlyDateFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditReportConfiguration {

  @Bean
  AuditReportGenerator auditReportGenerator(AuditingFinder auditingFinder) {
    return new AuditReportGenerator(
        DefaultTimeSource.INSTANCE, DigitsOnlyDateFormatter.INSTANCE, auditingFinder);
  }
}
