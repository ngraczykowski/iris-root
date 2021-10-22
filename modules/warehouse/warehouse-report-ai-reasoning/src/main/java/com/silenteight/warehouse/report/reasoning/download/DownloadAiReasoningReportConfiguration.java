package com.silenteight.warehouse.report.reasoning.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningReportConfiguration {

  @Bean
  DownloadProductionAiReasoningReportUseCase downloadProductionAiReasoningReportUseCase(
      AiReasoningReportDataQuery query, ReportStorage reportStorage) {

    return new DownloadProductionAiReasoningReportUseCase(query, reportStorage);
  }

  @Bean
  DownloadSimulationAiReasoningReportUseCase downloadSimulationAiReasoningReportUseCase(
      AiReasoningReportDataQuery query, ReportStorage reportStorage) {

    return new DownloadSimulationAiReasoningReportUseCase(query, reportStorage);
  }
}
