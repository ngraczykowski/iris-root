package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateMetricsReportConfiguration {

  @Bean
  CreateProductionMetricsReportUseCase createProductionMetricsReportUseCase(
      MetricsReportService service,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Valid MetricsReportProperties properties) {

    return new CreateProductionMetricsReportUseCase(
        service,
        productionIndexerQuery,
        properties.getProduction());
  }

  @Bean
  CreateSimulationMetricsReportUseCase createSimulationMetricsReportUseCase(
      MetricsReportService service,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid MetricsReportProperties properties,
      TimeSource timeSource) {

    return new CreateSimulationMetricsReportUseCase(
        service,
        simulationIndexerQuery,
        properties.getSimulation(),
        timeSource);
  }
}
