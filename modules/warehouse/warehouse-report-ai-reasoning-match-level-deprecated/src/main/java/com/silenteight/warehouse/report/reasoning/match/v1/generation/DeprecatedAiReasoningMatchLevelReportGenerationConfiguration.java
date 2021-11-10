package com.silenteight.warehouse.report.reasoning.match.v1.generation;

import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiReasoningReportProperties.class)
class DeprecatedAiReasoningMatchLevelReportGenerationConfiguration {

  @Bean
  DeprecatedAiReasoningMatchLevelReportGenerationService deprecatedAiMatchReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new DeprecatedAiReasoningMatchLevelReportGenerationService(reportGenerationService);
  }
}
