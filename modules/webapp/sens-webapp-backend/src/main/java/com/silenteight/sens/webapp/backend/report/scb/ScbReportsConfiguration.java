package com.silenteight.sens.webapp.backend.report.scb;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ScbReportsConfiguration {

  @Bean
  EntitlementReportGenerator entitlementReportGenerator() {
    return new EntitlementReportGenerator(
        DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
  }

  @Bean
  SecurityMatrixReportGenerator securityMatrixReportGenerator() {
    return new SecurityMatrixReportGenerator();
  }
}
