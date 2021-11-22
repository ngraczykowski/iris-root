package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateRbsReportConfiguration {

  @Bean
  CreateProductionRbsReportUseCase createProductionRbsReportUseCase(
      RbsReportService service, @Valid RbsReportProperties properties) {

    return new CreateProductionRbsReportUseCase(service, properties.getProduction());
  }

  @Bean
  CreateSimulationRbsReportUseCase createSimulationRbsReportUseCase(
      RbsReportService service,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid RbsReportProperties properties,
      TimeSource timeSource) {

    return new CreateSimulationRbsReportUseCase(
        service,
        simulationIndexerQuery,
        properties.getSimulation(),
        timeSource);
  }
}
