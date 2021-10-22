package com.silenteight.warehouse.report.reasoning.v1.domain;

import com.silenteight.warehouse.report.reasoning.v1.generation.DeprecatedAiReasoningReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedAiReasoningReportConfiguration {

  @Bean
  DeprecatedAiReasoningReportService deprecatedAiReasoningReportService(
      DeprecatedAiReasoningReportRepository reportRepository,
      DeprecatedAsyncAiReasoningReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new DeprecatedAiReasoningReportService(
        reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  DeprecatedAsyncAiReasoningReportGenerationService deprecatedAsyncAiReasoningService(
      DeprecatedAiReasoningReportRepository reportRepository,
      DeprecatedAiReasoningReportGenerationService reportGenerationService) {

    return new DeprecatedAsyncAiReasoningReportGenerationService(
        reportRepository, reportGenerationService, INSTANCE);
  }

  @Bean
  DeprecatedAiReasoningReportQuery deprecatedAiReasoningReportQuery(
      DeprecatedAiReasoningReportRepository repository) {

    return new DeprecatedAiReasoningReportQuery(repository);
  }
}
