package com.silenteight.warehouse.report.metrics.list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListSimulationConfiguration {

  @Bean
  MetricsReportProvider simulationMetricsSimulationReportsList() {
    return new MetricsReportProvider();
  }
}
