package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class AiReasoningReportConfiguration {

  @Bean
  AiReasoningReportService aiReasoningReportService(
      AiReasoningReportRepository reportRepository,
      AsyncAiReasoningReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new AiReasoningReportService(
        reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  AsyncAiReasoningReportGenerationService asyncAiReasoningReportGenerationService(
      AiReasoningReportRepository reportRepository,
      AiReasoningReportGenerationService reportGenerationService) {

    return new AsyncAiReasoningReportGenerationService(reportRepository, reportGenerationService);
  }

  @Bean
  AiReasoningReportQuery aiReasoningReportQuery(AiReasoningReportRepository repository) {
    return new AiReasoningReportQuery(repository);
  }

  @Bean
  SimulationAiReasoningReportProvider simulationAiReasoningReportProvider() {
    return new SimulationAiReasoningReportProvider();
  }
}
