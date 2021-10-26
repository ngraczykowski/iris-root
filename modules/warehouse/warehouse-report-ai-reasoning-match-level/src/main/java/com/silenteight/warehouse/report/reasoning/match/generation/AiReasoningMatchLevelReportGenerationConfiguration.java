package com.silenteight.warehouse.report.reasoning.match.generation;

import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiReasoningReportProperties.class)
class AiReasoningMatchLevelReportGenerationConfiguration {

  @Bean
  AiReasoningMatchLevelReportGenerationService aiReasoningMatchLevelReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new AiReasoningMatchLevelReportGenerationService(reportGenerationService);
  }
}
