package com.silenteight.warehouse.report.reasoning.match.download;

import com.silenteight.sep.base.common.time.IsoUtcWithoutMillisDateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningMatchLevelReportConfiguration {

  @Bean
  DownloadProductionAiReasoningMatchLevelReportUseCase downloadProductionAiMatchLevelReportUseCase(
      AiReasoningMatchLevelReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName productionReportFileNameService) {

    return new DownloadProductionAiReasoningMatchLevelReportUseCase(
        query,
        reportStorage,
        productionReportFileNameService,
        IsoUtcWithoutMillisDateFormatter.INSTANCE);
  }

  @Bean
  DownloadSimulationAiReasoningMatchLevelReportUseCase downloadSimulationAiMatchLevelReportUseCase(
      AiReasoningMatchLevelReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName simulationReportFileNameService) {

    return new DownloadSimulationAiReasoningMatchLevelReportUseCase(
        query, reportStorage, simulationReportFileNameService);
  }
}
