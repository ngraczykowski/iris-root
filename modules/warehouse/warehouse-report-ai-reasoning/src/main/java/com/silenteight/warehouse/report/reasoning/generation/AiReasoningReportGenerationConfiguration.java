package com.silenteight.warehouse.report.reasoning.generation;

import com.silenteight.warehouse.report.reporting.ReportGenerationService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class AiReasoningReportGenerationConfiguration {

  @Bean
  AiReasoningReportGenerationService aiReasoningReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new AiReasoningReportGenerationService(reportGenerationService);
  }
}
