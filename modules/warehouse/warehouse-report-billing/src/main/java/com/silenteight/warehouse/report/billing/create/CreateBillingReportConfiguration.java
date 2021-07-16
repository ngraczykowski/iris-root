package com.silenteight.warehouse.report.billing.create;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateBillingReportConfiguration {

  @Bean
  CreateBillingReportUseCase createBillingReportUseCase(BillingReportService reportService) {
    return new CreateBillingReportUseCase(reportService);
  }
}
