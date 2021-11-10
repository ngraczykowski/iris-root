package com.silenteight.warehouse.report.reasoning.match.v1.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedDownloadAiReasoningMatchLevelReportConfiguration {

  @Bean
  DeprecatedDownloadAiReasoningMatchLevelReportUseCase deprecatedDownloadAiMatchLevelReportUseCase(
      DeprecatedAiReasoningMatchLevelReportDataQuery query,
      ReportStorage reportStorage) {

    return new DeprecatedDownloadAiReasoningMatchLevelReportUseCase(query, reportStorage);
  }
}
