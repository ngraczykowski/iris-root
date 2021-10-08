package com.silenteight.warehouse.report.reasoning.download;

import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DownloadAiReasoningReportConfiguration {

  @Bean
  DownloadAiReasoningReportUseCase downloadAiReasoningReportUseCase(
      AiReasoningReportDataQuery query,
      ReportStorage reportStorage) {

    return new DownloadAiReasoningReportUseCase(query, reportStorage);
  }
}
