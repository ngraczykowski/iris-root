package com.silenteight.warehouse.report.accuracy.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListAccuracyReportsConfiguration {

  @Bean
  AccuracyReportProvider accuracySimulationReportProvider() {
    return new AccuracyReportProvider();
  }
}
