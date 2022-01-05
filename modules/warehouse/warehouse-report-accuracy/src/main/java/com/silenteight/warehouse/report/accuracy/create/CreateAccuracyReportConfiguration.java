package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
class CreateAccuracyReportConfiguration {

  @Bean
  CreateSimulationAccuracyReportUseCase createSimulationAccuracyReportUseCase(
      AccuracyReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {

    if (Objects.isNull(properties.getAccuracy().getProduction())) {
      return null;
    } else {
      return new CreateSimulationAccuracyReportUseCase(
          service,
          properties.getAccuracy().getSimulation(),
          simulationIndexerQuery,
          timeSource);
    }
  }

  @Bean
  CreateProductionAccuracyReportUseCase createProductionAccuracyReportUseCase(
      AccuracyReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery indexesQuery) {

    if (Objects.isNull(properties.getAccuracy().getSimulation())) {
      return null;
    } else {
      return new CreateProductionAccuracyReportUseCase(
          service,
          properties.getAccuracy().getProduction(),
          indexesQuery);
    }
  }
}
