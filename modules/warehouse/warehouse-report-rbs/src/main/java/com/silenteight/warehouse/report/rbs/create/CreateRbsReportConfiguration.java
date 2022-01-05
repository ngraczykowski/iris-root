package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
class CreateRbsReportConfiguration {

  @Bean
  CreateProductionRbsReportUseCase createProductionRbsReportUseCase(
      RbsReportService service, @Valid ReportProperties properties) {
    //@ConditionalOnProperty("warehouse.report.rbs.production") does not work here,
    // properties are not visible here
    if (Objects.isNull(properties.getRbs().getProduction())) {
      return null;
    } else {
      return new CreateProductionRbsReportUseCase(service, properties.getRbs().getProduction());
    }
  }

  @Bean
  CreateSimulationRbsReportUseCase createSimulationRbsReportUseCase(
      RbsReportService service,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid ReportProperties properties,
      TimeSource timeSource) {

    if (Objects.isNull(properties.getRbs().getSimulation())) {
      return null;
    } else {
      return new CreateSimulationRbsReportUseCase(
          service,
          simulationIndexerQuery,
          properties.getRbs().getSimulation(),
          timeSource);
    }
  }
}
