package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class AiReasoningMatchLevelReportConfiguration {

  @Bean
  AiReasoningMatchLevelReportService aiReasoningMatchLevelReportService(
      AiReasoningMatchLevelReportRepository reportRepository,
      AsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new AiReasoningMatchLevelReportService(
        reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  AsyncAiReasoningMatchLevelReportGenerationService asyncAiReasoningMatchReportGenerationService(
      AiReasoningMatchLevelReportRepository reportRepository,
      AiReasoningMatchLevelReportGenerationService reportGenerationService) {

    return new AsyncAiReasoningMatchLevelReportGenerationService(
        reportRepository, reportGenerationService);
  }

  @Bean
  AiReasoningMatchLevelReportQuery aiReasoningMatchLevelReportQuery(
      AiReasoningMatchLevelReportRepository repository) {
    return new AiReasoningMatchLevelReportQuery(repository);
  }

  @Bean
  @ConditionalOnProperty(
      value = "warehouse.report.ai-reasoning-match-level.simulation.visible",
      matchIfMissing = true)
  SimulationAiReasoningMatchLevelReportProvider aiReasoningMatchLevelReportProvider() {
    return new SimulationAiReasoningMatchLevelReportProvider();
  }
}
