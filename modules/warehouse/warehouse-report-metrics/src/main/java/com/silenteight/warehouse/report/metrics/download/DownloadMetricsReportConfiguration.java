package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadMetricsReportConfiguration {

  @Bean
  DownloadProductionMetricsReportUseCase downloadProductionMetricsReportUseCase(
      MetricsReportService reportService,
      MetricsReportDataQuery query,
      ReportFileName productionReportFileNameService,
      ReportStorage reportStorage) {

    return new DownloadProductionMetricsReportUseCase(
        query,
        reportService,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE,
        reportStorage);
  }

  @Bean
  DownloadSimulationMetricsReportUseCase downloadSimulationMetricsReportUseCase(
      MetricsReportService reportService,
      MetricsReportDataQuery query,
      ReportFileName simulationReportFileNameService,
      ReportStorage reportStorage) {

    return new DownloadSimulationMetricsReportUseCase(
        query, reportService, simulationReportFileNameService, reportStorage);
  }
}
