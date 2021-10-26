package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class AccuracyReportConfiguration {

  @Bean
  AccuracyReportService accuracyReportService(
      AccuracyReportRepository reportRepository,
      AsyncAccuracyReportGenerationService asyncReportGenerationService,
      ReportStorage reportStorage) {

    return new AccuracyReportService(reportRepository, asyncReportGenerationService, reportStorage);
  }

  @Bean
  AsyncAccuracyReportGenerationService asyncAccuracyReportGenerationService(
      AccuracyReportRepository reportRepository,
      AccuracyReportGenerationService reportGenerationService) {

    return new AsyncAccuracyReportGenerationService(reportRepository, reportGenerationService);
  }

  @Bean
  AccuracyReportQuery accuracyReportQuery(AccuracyReportRepository repository) {
    return new AccuracyReportQuery(repository);
  }

  @Bean
  SimulationAccuracyReportProvider simulationAccuracyReportProvider() {
    return new SimulationAccuracyReportProvider();
  }
}
