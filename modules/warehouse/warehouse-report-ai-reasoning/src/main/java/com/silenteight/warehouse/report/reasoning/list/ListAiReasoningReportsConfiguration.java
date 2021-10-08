package com.silenteight.warehouse.report.reasoning.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListAiReasoningReportsConfiguration {

  @Bean
  AiReasoningReportProvider aiReasoningReportProvider() {
    return new AiReasoningReportProvider();
  }
}
