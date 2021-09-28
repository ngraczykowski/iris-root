package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateAccuracyReportConfiguration {

  @Bean
  CreateAccuracyReportUseCase createAccuracyReportUseCase(
      AccuracyReportService service,
      @Valid AccuracyReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {

    return new CreateAccuracyReportUseCase(
        service,
        properties.getProduction(),
        properties.getSimulation(),
        productionIndexerQuery,
        simulationIndexerQuery);
  }
}
