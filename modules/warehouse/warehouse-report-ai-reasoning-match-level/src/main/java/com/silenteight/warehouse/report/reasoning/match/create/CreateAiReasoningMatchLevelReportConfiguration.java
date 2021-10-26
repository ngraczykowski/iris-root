package com.silenteight.warehouse.report.reasoning.match.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateAiReasoningMatchLevelReportConfiguration {

  @Bean
  CreateProductionAiReasoningMatchLevelReportUseCase aiReasoningMatchLevelProductionUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "productionMatchIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new CreateProductionAiReasoningMatchLevelReportUseCase(
        service,
        properties.getProduction(),
        productionIndexerQuery);
  }

  @Bean
  CreateSimulationAiReasoningMatchLevelReportUseCase aiReasoningMatchLevelSimulationUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {

    return new CreateSimulationAiReasoningMatchLevelReportUseCase(
        service,
        properties.getSimulation(),
        simulationIndexerQuery);
  }
}
