package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadMetricsReportConfiguration {

  @Bean
  DownloadMetricsReportUseCase downloadMetricsReportUseCase(
      MetricsReportService reportService, MetricsReportDataQuery query) {

    return new DownloadMetricsReportUseCase(query, reportService);
  }
}
