package com.silenteight.warehouse.report.sm.download;

import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadSimulationMetricsReportConfiguration {

  @Bean
  DownloadSimulationMetricsReportUseCase downloadSimulationMetricsReportUseCase(
      SimulationMetricsReportService reportService, SimulationMetricsReportDataQuery query) {
    return new DownloadSimulationMetricsReportUseCase(query, reportService);
  }
}
