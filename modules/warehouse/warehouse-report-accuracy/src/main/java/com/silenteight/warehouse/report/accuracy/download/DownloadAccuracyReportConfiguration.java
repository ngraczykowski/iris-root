package com.silenteight.warehouse.report.accuracy.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAccuracyReportConfiguration {

  @Bean
  DownloadProductionAccuracyReportUseCase downloadProductionAccuracyReportUseCase(
      AccuracyReportDataQuery query,
      ReportStorage reportStorage) {

    return new DownloadProductionAccuracyReportUseCase(query, reportStorage);
  }

  @Bean
  DownloadSimulationAccuracyReportUseCase downloadSimulationAccuracyReportUseCase(
      AccuracyReportDataQuery query,
      ReportStorage reportStorage) {

    return new DownloadSimulationAccuracyReportUseCase(query, reportStorage);
  }
}
