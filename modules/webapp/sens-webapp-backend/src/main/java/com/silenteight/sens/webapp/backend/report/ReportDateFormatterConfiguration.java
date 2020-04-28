package com.silenteight.sens.webapp.backend.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReportDateFormatterConfiguration {

  @Bean
  ReportDateFormatter reportDateFormatter() {
    return new ReportDateFormatter();
  }
}
