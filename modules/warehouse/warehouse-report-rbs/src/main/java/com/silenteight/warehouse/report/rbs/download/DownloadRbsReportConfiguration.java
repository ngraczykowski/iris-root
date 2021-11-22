package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadRbsReportConfiguration {

  @Bean
  DownloadProductionRbsReportUseCase downloadProductionRbsReportUseCase(
      RbsReportService reportService,
      RbsReportDataQuery query,
      ReportFileName productionReportFileNameService) {

    return new DownloadProductionRbsReportUseCase(
        query, reportService, productionReportFileNameService);
  }

  @Bean
  DownloadSimulationRbsReportUseCase downloadSimulationRbsReportUseCase(
      RbsReportService reportService,
      RbsReportDataQuery query,
      ReportFileName simulationReportFileNameService) {

    return new DownloadSimulationRbsReportUseCase(
        query, reportService, simulationReportFileNameService);
  }
}
