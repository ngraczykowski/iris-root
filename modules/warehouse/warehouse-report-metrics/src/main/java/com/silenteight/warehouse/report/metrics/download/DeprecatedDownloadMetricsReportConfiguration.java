package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedDownloadMetricsReportConfiguration {

  @Bean
  DeprecatedDownloadMetricsReportUseCase deprecatedDownloadMetricsReportUseCase(
      DeprecatedMetricsReportService reportService, DeprecatedMetricsReportDataQuery query) {

    return new DeprecatedDownloadMetricsReportUseCase(query, reportService);
  }
}
