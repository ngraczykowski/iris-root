package com.silenteight.warehouse.report.reasoning.download;

import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningReportConfiguration {

  @Bean
  DownloadProductionAiReasoningReportUseCase downloadProductionAiReasoningReportUseCase(
      AiReasoningReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName productionReportFileNameService) {

    return new DownloadProductionAiReasoningReportUseCase(
        query, reportStorage, productionReportFileNameService);
  }

  @Bean
  DownloadSimulationAiReasoningReportUseCase downloadSimulationAiReasoningReportUseCase(
      AiReasoningReportDataQuery query,
      ReportStorage reportStorage,
      ReportFileName simulationReportFileNameService) {

    return new DownloadSimulationAiReasoningReportUseCase(
        query, reportStorage, simulationReportFileNameService);
  }
}
