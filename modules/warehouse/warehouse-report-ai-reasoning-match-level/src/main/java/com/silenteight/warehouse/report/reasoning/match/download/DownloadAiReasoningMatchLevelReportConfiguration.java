package com.silenteight.warehouse.report.reasoning.match.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningMatchLevelReportConfiguration {

  @Bean
  DownloadAiReasoningMatchLevelReportUseCase downloadAiReasoningMatchLevelReportUseCase(
      AiReasoningMatchLevelReportDataQuery query,
      ReportStorage reportStorage) {

    return new DownloadAiReasoningMatchLevelReportUseCase(query, reportStorage);
  }
}
