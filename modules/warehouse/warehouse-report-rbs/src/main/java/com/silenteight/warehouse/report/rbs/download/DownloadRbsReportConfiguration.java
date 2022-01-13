package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadRbsReportConfiguration {

  @Bean
  DownloadProductionRbsReportUseCase downloadProductionRbsReportUseCase(
      RbsReportService reportService,
      RbsReportDataQuery query,
      ReportFileName productionReportFileNameService,
      ReportStorage reportStorage) {

    return new DownloadProductionRbsReportUseCase(
        query,
        reportService,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE,
        reportStorage
    );
  }

  @Bean
  DownloadSimulationRbsReportUseCase downloadSimulationRbsReportUseCase(
      RbsReportService reportService,
      RbsReportDataQuery query,
      ReportFileName simulationReportFileNameService,
      ReportStorage reportStorage) {

    return new DownloadSimulationRbsReportUseCase(
        query, reportService, simulationReportFileNameService, reportStorage);
  }
}
