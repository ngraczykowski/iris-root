package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.name.ReportFileName;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadMetricsReportConfiguration {

  @Bean
  DownloadProductionMetricsReportUseCase downloadProductionMetricsReportUseCase(
      MetricsReportService reportService,
      MetricsReportDataQuery query,
      ReportFileName productionReportFileNameService) {

    return new DownloadProductionMetricsReportUseCase(
        query, reportService, productionReportFileNameService);
  }

  @Bean
  DownloadSimulationMetricsReportUseCase downloadSimulationMetricsReportUseCase(
      MetricsReportService reportService,
      MetricsReportDataQuery query,
      ReportFileName simulationReportFileNameService) {

    return new DownloadSimulationMetricsReportUseCase(
        query, reportService, simulationReportFileNameService);
  }
}
