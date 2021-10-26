package com.silenteight.warehouse.report.accuracy.v1.generation;

import com.silenteight.warehouse.report.reporting.ReportGenerationService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccuracyReportProperties.class)
class DeprecatedAccuracyReportGenerationConfiguration {

  @Bean
  DeprecatedAccuracyReportGenerationService deprecatedAccuracyReportGenerationService(
      ReportGenerationService reportGenerationService) {

    return new DeprecatedAccuracyReportGenerationService(reportGenerationService);
  }
}
