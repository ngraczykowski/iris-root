package com.silenteight.warehouse.report.reasoning.v1.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeprecatedListAiReasoningReportsConfiguration {

  @Bean
  DeprecatedAiReasoningReportProvider deprecatedAiReasoningReportProvider() {
    return new DeprecatedAiReasoningReportProvider();
  }
}
