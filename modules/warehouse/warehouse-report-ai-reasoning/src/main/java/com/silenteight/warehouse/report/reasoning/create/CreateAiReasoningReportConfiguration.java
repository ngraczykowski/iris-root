package com.silenteight.warehouse.report.reasoning.create;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import javax.validation.Valid;

@Configuration
class CreateAiReasoningReportConfiguration {

  @Bean
  CreateProductionAiReasoningReportUseCase createProductionAiReasoningReportUseCase(
      AiReasoningReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery) {

    if (Objects.isNull(properties.getAccuracy().getProduction())) {
      return null;
    } else {
      return new CreateProductionAiReasoningReportUseCase(
          service,
          properties,
          productionIndexerQuery);
    }
  }

  @Bean
  CreateSimulationAiReasoningReportUseCase createSimulationAiReasoningReportUseCase(
      AiReasoningReportService service,
      @Valid ReportProperties properties,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      TimeSource timeSource) {

    if (Objects.isNull(properties.getAiReasoning().getSimulation())) {
      return null;
    } else {
      return new CreateSimulationAiReasoningReportUseCase(
          service,
          properties.getAiReasoning().getSimulation(),
          simulationIndexerQuery,
          timeSource);
    }
  }
}
