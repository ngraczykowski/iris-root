package com.silenteight.warehouse.report.metrics.v1.download;

import com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportService;

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
