package com.silenteight.warehouse.report.reasoning.v1.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedDownloadAiReasoningReportConfiguration {

  @Bean
  DeprecatedDownloadAiReasoningReportUseCase deprecatedDownloadAiReasoningReportUseCase(
      DeprecatedAiReasoningReportDataQuery query,
      ReportStorage reportStorage) {

    return new DeprecatedDownloadAiReasoningReportUseCase(query, reportStorage);
  }
}
