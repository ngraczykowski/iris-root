package com.silenteight.warehouse.report.reasoning.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportService;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class CreateAiReasoningReportConfiguration {

  @Bean
  CreateProductionAiReasoningReportUseCase createProductionAiReasoningReportUseCase(
      AiReasoningReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new CreateProductionAiReasoningReportUseCase(
        service,
        properties.getProduction(),
        productionIndexerQuery);
  }

  @Bean
  CreateSimulationAiReasoningReportUseCase createSimulationAiReasoningReportUseCase(
      AiReasoningReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {

    return new CreateSimulationAiReasoningReportUseCase(
        service,
        properties.getSimulation(),
        simulationIndexerQuery,
        timeSource);
  }
}
