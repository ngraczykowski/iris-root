package com.silenteight.warehouse.report.metrics.list;

import org.springframework.context.annotation.Bean;

public class ListSimulationConfiguration {

  @Bean
  MetricsReportProvider simulationMetricsSimulationReportsList() {
    return new MetricsReportProvider();
  }
}
