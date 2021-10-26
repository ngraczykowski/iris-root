package com.silenteight.warehouse.report.reasoning.match.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListAiReasoningMatchLevelReportsConfiguration {

  @Bean
  AiReasoningMatchLevelReportProvider aiReasoningMatchLevelReportProvider() {
    return new AiReasoningMatchLevelReportProvider();
  }
}
