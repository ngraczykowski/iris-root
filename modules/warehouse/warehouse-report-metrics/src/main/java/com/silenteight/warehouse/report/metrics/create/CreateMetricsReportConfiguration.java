package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
class CreateMetricsReportConfiguration {

  @Bean
  CreateProductionMetricsReportUseCase createProductionMetricsReportUseCase(
      MetricsReportService service,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Valid ReportProperties properties) {

    if (Objects.isNull(properties.getMetrics().getProduction())) {
      return null;
    } else {
      return new CreateProductionMetricsReportUseCase(
          service,
          productionIndexerQuery,
          properties.getMetrics().getProduction());
    }
  }

  @Bean
  CreateSimulationMetricsReportUseCase createSimulationMetricsReportUseCase(
      MetricsReportService service,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid ReportProperties properties,
      TimeSource timeSource) {

    if (Objects.isNull(properties.getMetrics().getSimulation())) {
      return null;
    } else {
      return new CreateSimulationMetricsReportUseCase(
          service,
          simulationIndexerQuery,
          properties.getMetrics().getSimulation(),
          timeSource);
    }

  }
}
