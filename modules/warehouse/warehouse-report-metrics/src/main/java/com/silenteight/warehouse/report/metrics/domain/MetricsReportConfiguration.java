package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class MetricsReportConfiguration {

  @Bean
  MetricsReportService metricsReportService(
      MetricsReportRepository reportRepository,
      AsyncMetricsReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new MetricsReportService(
        reportRepository,
        asyncReportGenerationService,
        reportStorage);
  }

  @Bean
  AsyncMetricsReportGenerationService asyncMetricsReportGenerationService(
      MetricsReportRepository reportRepository,
      MetricsReportGenerationService reportGenerationService,
      ReportStorage reportStorage) {

    return new AsyncMetricsReportGenerationService(
        reportRepository,
        reportGenerationService,
        reportStorage);
  }

  @Bean
  MetricsReportQuery metricsReportQuery(MetricsReportRepository repository) {
    return new MetricsReportQuery(repository);
  }

  @Bean
  @ConditionalOnProperty(
      value = "warehouse.report.metrics.simulation.visible",
      matchIfMissing = true)
  SimulationMetricsReportProvider simulationMetricsReportProvider() {
    return new SimulationMetricsReportProvider();
  }
}
