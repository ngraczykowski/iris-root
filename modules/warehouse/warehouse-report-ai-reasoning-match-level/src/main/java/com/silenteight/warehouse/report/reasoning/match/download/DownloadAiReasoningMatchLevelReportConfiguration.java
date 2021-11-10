package com.silenteight.warehouse.report.reasoning.match.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningMatchLevelReportConfiguration {

  @Bean
  DownloadProductionAiReasoningMatchLevelReportUseCase downloadProductionAiMatchLevelReportUseCase(
      AiReasoningMatchLevelReportDataQuery query, ReportStorage reportStorage) {

    return new DownloadProductionAiReasoningMatchLevelReportUseCase(query, reportStorage);
  }

  @Bean
  DownloadSimulationAiReasoningMatchLevelReportUseCase downloadSimulationAiMatchLevelReportUseCase(
      AiReasoningMatchLevelReportDataQuery query, ReportStorage reportStorage) {

    return new DownloadSimulationAiReasoningMatchLevelReportUseCase(query, reportStorage);
  }
}
