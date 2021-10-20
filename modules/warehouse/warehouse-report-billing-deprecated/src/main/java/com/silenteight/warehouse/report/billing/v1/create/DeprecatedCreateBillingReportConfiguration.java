package com.silenteight.warehouse.report.billing.v1.create;

import com.silenteight.warehouse.report.billing.v1.domain.DeprecatedBillingReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedCreateBillingReportConfiguration {

  @Bean
  DeprecatedCreateBillingReportUseCase deprecatedCreateBillingReportUseCase(
      DeprecatedBillingReportService reportService) {

    return new DeprecatedCreateBillingReportUseCase(reportService);
  }
}
