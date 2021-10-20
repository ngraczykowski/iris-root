package com.silenteight.warehouse.report.billing.download;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadBillingReportConfiguration {

  @Bean
  DownloadBillingReportUseCase downloadBillingReportUseCase(
      ReportDataQuery query, BillingReportService reportService) {

    return new DownloadBillingReportUseCase(query, reportService);
  }
}
