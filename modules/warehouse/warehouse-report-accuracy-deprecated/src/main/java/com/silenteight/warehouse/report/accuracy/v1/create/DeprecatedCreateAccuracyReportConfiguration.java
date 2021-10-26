package com.silenteight.warehouse.report.accuracy.v1.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportService;
import com.silenteight.warehouse.report.accuracy.v1.generation.AccuracyReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class DeprecatedCreateAccuracyReportConfiguration {

  @Bean
  DeprecatedCreateAccuracyReportUseCase deprecatedCreateAccuracyReportUseCase(
      DeprecatedAccuracyReportService service,
      @Valid AccuracyReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {

    return new DeprecatedCreateAccuracyReportUseCase(
        service,
        properties.getProduction(),
        properties.getSimulation(),
        productionIndexerQuery,
        simulationIndexerQuery);
  }
}
