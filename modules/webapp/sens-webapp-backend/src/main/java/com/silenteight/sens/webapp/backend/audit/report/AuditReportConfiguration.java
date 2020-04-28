package com.silenteight.sens.webapp.backend.audit.report;

import com.silenteight.sens.webapp.common.time.DateFormatter;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditReportConfiguration {

  @Bean
  AuditReportGenerator auditReportGenerator(DateFormatter dateFormatter) {
    return new AuditReportGenerator(DefaultTimeSource.INSTANCE, dateFormatter);
  }
}
