package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedAiReasoningMatchLevelReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedAiReasoningMatchLevelReportConfiguration {

  @Bean
  DeprecatedAiReasoningMatchLevelReportService deprecatedAiReasoningMatchLevelReportService(
      DeprecatedAiReasoningMatchLevelReportRepository reportRepository,
      DeprecatedAsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new DeprecatedAiReasoningMatchLevelReportService(
        reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  DeprecatedAsyncAiReasoningMatchLevelReportGenerationService deprecatedAsyncAiMatchReportService(
      DeprecatedAiReasoningMatchLevelReportRepository reportRepository,
      DeprecatedAiReasoningMatchLevelReportGenerationService reportGenerationService) {

    return new DeprecatedAsyncAiReasoningMatchLevelReportGenerationService(
        reportRepository, reportGenerationService, INSTANCE);
  }

  @Bean
  DeprecatedAiReasoningMatchLevelReportQuery deprecatedAiReasoningMatchLevelReportQuery(
      DeprecatedAiReasoningMatchLevelReportRepository repository) {

    return new DeprecatedAiReasoningMatchLevelReportQuery(repository);
  }
}
