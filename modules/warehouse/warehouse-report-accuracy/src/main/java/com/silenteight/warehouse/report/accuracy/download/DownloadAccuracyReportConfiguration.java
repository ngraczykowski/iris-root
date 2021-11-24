package com.silenteight.warehouse.report.accuracy.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAccuracyReportConfiguration {

  @Bean
  DownloadProductionAccuracyReportUseCase downloadProductionAccuracyReportUseCase(
      AccuracyReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName productionReportFileNameService) {

    return new DownloadProductionAccuracyReportUseCase(
        query,
        reportStorage,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE);
  }

  @Bean
  DownloadSimulationAccuracyReportUseCase downloadSimulationAccuracyReportUseCase(
      AccuracyReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName simulationReportFileNameService) {

    return new DownloadSimulationAccuracyReportUseCase(
        query, reportStorage, simulationReportFileNameService);
  }
}
