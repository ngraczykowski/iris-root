package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;

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
  AsyncAiReasoningMatchLevelReportGenerationService asyncMatchLevelReportGenerationService(
      AiReasoningMatchLevelReportRepository reportRepository,
      AiReasoningMatchLevelReportGenerationService reportGenerationService) {

    return new AsyncAiReasoningMatchLevelReportGenerationService(
        reportRepository, reportGenerationService, INSTANCE);
  }

  @Bean
  AiReasoningMatchLevelReportQuery aiReasoningMatchLevelReportQuery(
      AiReasoningMatchLevelReportRepository repository) {

    return new AiReasoningMatchLevelReportQuery(repository);
  }
}
