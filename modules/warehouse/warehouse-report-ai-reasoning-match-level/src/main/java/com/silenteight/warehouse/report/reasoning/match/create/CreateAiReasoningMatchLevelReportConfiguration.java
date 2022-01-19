package com.silenteight.warehouse.report.reasoning.match.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reporting.AiReasoningMatchLevelReportProperties;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import javax.validation.Valid;

@Configuration
class CreateAiReasoningMatchLevelReportConfiguration {

  @Bean
  CreateProductionAiReasoningMatchLevelReportUseCase createProductionAiReasoningMatchReportUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "productionMatchIndexingQuery") IndexesQuery productionIndexerQuery) {
    if (Optional.ofNullable(properties.getAiReasoningMatchLevel()).map(
        AiReasoningMatchLevelReportProperties::getProduction).isEmpty()) {
      return null;
    } else {
      return new CreateProductionAiReasoningMatchLevelReportUseCase(
          service,
          properties.getAiReasoningMatchLevel().getProduction(),
          productionIndexerQuery);
    }
  }

  @Bean
  CreateSimulationAiReasoningMatchLevelReportUseCase createSimulationAiReasoningMatchReportUseCase(
      AiReasoningMatchLevelReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {
    if (Optional.ofNullable(properties.getAiReasoningMatchLevel()).map(
        AiReasoningMatchLevelReportProperties::getSimulation).isEmpty()) {
      return null;
    } else {
      return new CreateSimulationAiReasoningMatchLevelReportUseCase(
          service,
          properties.getAiReasoningMatchLevel().getSimulation(),
          simulationIndexerQuery,
          timeSource);
    }
  }
}
