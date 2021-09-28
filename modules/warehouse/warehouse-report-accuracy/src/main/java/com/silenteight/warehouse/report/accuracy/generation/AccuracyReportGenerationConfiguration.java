package com.silenteight.warehouse.report.accuracy.generation;


import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccuracyReportProperties.class)
class AccuracyReportGenerationConfiguration {

  @Bean
  AccuracyReportGenerationService accuracyReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new AccuracyReportGenerationService(reportGenerationService);
  }
}
