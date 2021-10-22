package com.silenteight.warehouse.report.reasoning.v1.generation;

import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiReasoningReportProperties.class)
class DeprecatedAiReasoningReportGenerationConfiguration {

  @Bean
  DeprecatedAiReasoningReportGenerationService deprecatedAiReasoningReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new DeprecatedAiReasoningReportGenerationService(reportGenerationService);
  }
}
