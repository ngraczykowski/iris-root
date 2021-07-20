package com.silenteight.warehouse.report.sm.create;

import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateSimulationMetricsReportConfiguration {

  @Bean
  CreateSimulationMetricsReportUseCase createSimulationMetricsReportUseCase(
      SimulationMetricsReportService service) {
    return new CreateSimulationMetricsReportUseCase(service);
  }
}
