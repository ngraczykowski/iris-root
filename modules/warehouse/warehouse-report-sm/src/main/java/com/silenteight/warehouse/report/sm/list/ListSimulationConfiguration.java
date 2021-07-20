package com.silenteight.warehouse.report.sm.list;

import org.springframework.context.annotation.Bean;

public class ListSimulationConfiguration {

  @Bean
  SimulationMetricsReportProvider simulationMetricsSimulationReportsList() {
    return new SimulationMetricsReportProvider();
  }
}
