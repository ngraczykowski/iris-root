package com.silenteight.warehouse.report.reasoning.match.v1.create;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedAiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.v1.generation.AiReasoningReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class DeprecatedCreateAiReasoningMatchLevelReportConfiguration {

  @Bean
  DeprecatedCreateProductionAiReasoningMatchLevelReportUseCase deprecatedCreateAiMatchReportUseCase(
      DeprecatedAiReasoningMatchLevelReportService service,
      @Valid AiReasoningReportProperties properties,
      @Qualifier(value = "productionMatchIndexingQuery") IndexesQuery productionIndexerQuery) {

    return new DeprecatedCreateProductionAiReasoningMatchLevelReportUseCase(
        service,
        properties.getProduction(),
        productionIndexerQuery);
  }
}
