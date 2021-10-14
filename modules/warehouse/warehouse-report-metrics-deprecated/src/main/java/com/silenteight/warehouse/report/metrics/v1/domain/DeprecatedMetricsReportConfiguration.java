package com.silenteight.warehouse.report.metrics.v1.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.v1.generation.DeprecatedMetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.v1.generation.MetricsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.validation.Valid;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedMetricsReportConfiguration {

  @Bean
  DeprecatedMetricsReportService deprecatedMetricsReportService(
      DeprecatedMetricsReportRepository reportRepository,
      DeprecatedAsyncMetricsReportGenerationService asyncReportGenerationService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid MetricsReportProperties properties) {

    return new DeprecatedMetricsReportService(
        reportRepository,
        asyncReportGenerationService,
        productionIndexerQuery,
        simulationIndexerQuery,
        properties.getProduction(),
        properties.getSimulation());
  }

  @Bean
  DeprecatedAsyncMetricsReportGenerationService deprecatedAsyncMetricsReportGenerationService(
      DeprecatedMetricsReportRepository reportRepository,
      DeprecatedMetricsReportGenerationService reportGenerationService) {

    return new DeprecatedAsyncMetricsReportGenerationService(
        reportRepository, reportGenerationService,
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  DeprecatedMetricsReportQuery deprecatedMetricsReportQuery(
      DeprecatedMetricsReportRepository repository) {

    return new DeprecatedMetricsReportQuery(repository);
  }
}
