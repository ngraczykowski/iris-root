package com.silenteight.warehouse.report.reasoning.v1.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportService;
import com.silenteight.warehouse.report.reasoning.v1.generation.AiReasoningReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class DeprecatedCreateAiReasoningReportConfiguration {

  @Bean
  DeprecatedCreateProductionAiReasoningReportUseCase aiReasoningProductionUseCase(
      DeprecatedAiReasoningReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new DeprecatedCreateProductionAiReasoningReportUseCase(
        service,
        properties.getProduction(),
        productionIndexerQuery);
  }

  @Bean
  DeprecatedCreateSimulationAiReasoningReportUseCase aiReasoningSimulationUseCase(
      DeprecatedAiReasoningReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {

    return new DeprecatedCreateSimulationAiReasoningReportUseCase(
        service,
        properties.getSimulation(),
        simulationIndexerQuery);
  }
}
