package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateAccuracyReportConfiguration {

  @Bean
  CreateSimulationAccuracyReportUseCase createSimulationAccuracyReportUseCase(
      AccuracyReportService service,
      @Valid AccuracyReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {

    return new CreateSimulationAccuracyReportUseCase(
        service,
        properties.getSimulation(),
        simulationIndexerQuery,
        timeSource);
  }

  @Bean
  CreateProductionAccuracyReportUseCase createProductionAccuracyReportUseCase(
      AccuracyReportService service,
      @Valid AccuracyReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery indexesQuery) {

    return new CreateProductionAccuracyReportUseCase(
        service,
        properties.getProduction(),
        indexesQuery);
  }
}
