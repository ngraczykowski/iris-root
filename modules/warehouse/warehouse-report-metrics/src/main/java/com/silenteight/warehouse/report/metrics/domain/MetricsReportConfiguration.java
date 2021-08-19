package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.validation.Valid;

@Configuration
@EntityScan
@EnableJpaRepositories
class MetricsReportConfiguration {

  @Bean
  MetricsReportService metricsReportService(
      MetricsReportRepository reportRepository,
      AsyncMetricsReportGenerationService asyncReportGenerationService,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery,
      @Valid MetricsReportProperties properties) {

    return new MetricsReportService(
        reportRepository,
        asyncReportGenerationService,
        productionIndexerQuery,
        simulationIndexerQuery,
        properties.getProduction(),
        properties.getSimulation());
  }

  @Bean
  AsyncMetricsReportGenerationService asyncMetricsReportGenerationService(
      MetricsReportRepository reportRepository,
      MetricsReportGenerationService reportGenerationService) {

    return new AsyncMetricsReportGenerationService(
        reportRepository, reportGenerationService,
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  MetricsReportQuery metricsReportQuery(MetricsReportRepository repository) {
    return new MetricsReportQuery(repository);
  }
}
