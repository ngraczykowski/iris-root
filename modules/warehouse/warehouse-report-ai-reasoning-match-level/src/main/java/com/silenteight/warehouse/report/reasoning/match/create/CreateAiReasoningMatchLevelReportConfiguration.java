package com.silenteight.warehouse.report.reasoning.match.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateAiReasoningMatchLevelReportConfiguration {

  @Bean
  CreateProductionAiReasoningMatchLevelReportUseCase createProductionAiReasoningMatchReportUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid AiReasoningMatchLevelReportProperties properties,
      @Qualifier(value = "productionMatchIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new CreateProductionAiReasoningMatchLevelReportUseCase(
        service,
        properties.getProduction(),
        productionIndexerQuery);
  }

  @Bean
  CreateSimulationAiReasoningMatchLevelReportUseCase createSimulationAiReasoningMatchReportUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid AiReasoningMatchLevelReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {

    return new CreateSimulationAiReasoningMatchLevelReportUseCase(
        service,
        properties.getSimulation(),
        simulationIndexerQuery,
        timeSource);
  }
}
