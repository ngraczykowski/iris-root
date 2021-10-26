package com.silenteight.warehouse.report.accuracy.v1.domain;

import com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedAccuracyReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedAccuracyReportConfiguration {

  @Bean
  DeprecatedAccuracyReportService deprecatedAccuracyReportService(
      DeprecatedAccuracyReportRepository reportRepository,
      DeprecatedAsyncAccuracyReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new DeprecatedAccuracyReportService(
        reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  DeprecatedAsyncAccuracyReportGenerationService deprecatedAsyncAccuracyReportGenerationService(
      DeprecatedAccuracyReportRepository reportRepository,
      DeprecatedAccuracyReportGenerationService reportGenerationService) {

    return new DeprecatedAsyncAccuracyReportGenerationService(
        reportRepository, reportGenerationService, INSTANCE);
  }

  @Bean
  DeprecatedAccuracyReportQuery deprecatedAccuracyReportQuery(
      DeprecatedAccuracyReportRepository repository) {

    return new DeprecatedAccuracyReportQuery(repository);
  }
}
