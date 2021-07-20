package com.silenteight.warehouse.report.sm.domain;

import com.silenteight.warehouse.report.sm.generation.SimulationMetricsReportGenerationService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class SimulationMetricsReportConfiguration {

  @Bean
  SimulationMetricsReportService simulationMetricsReportService(
      SimulationMetricsReportRepository reportRepository,
      AsyncSimulationMetricsReportGenerationService asyncReportGenerationService) {

    return new SimulationMetricsReportService(reportRepository, asyncReportGenerationService);
  }

  @Bean
  AsyncSimulationMetricsReportGenerationService asyncSimulationMetricsReportGenerationService(
      SimulationMetricsReportRepository reportRepository,
      SimulationMetricsReportGenerationService reportGenerationService) {

    return new AsyncSimulationMetricsReportGenerationService(
        reportRepository, reportGenerationService);
  }

  @Bean
  SimulationMetricsReportQuery simulationMetricsReportQuery(
      SimulationMetricsReportRepository repository) {
    return new SimulationMetricsReportQuery(repository);
  }
}
