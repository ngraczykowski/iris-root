package com.silenteight.warehouse.report.rbs.v1.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.v1.generation.RbsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class DeprecatedRbsReportConfiguration {

  @Bean
  DeprecatedRbsReportService deprecatedRbsReportService(
      DeprecatedRbsReportRepository rbsReportRepository,
      DeprecatedAsyncRbsReportGenerationService asyncReportGenerationService) {

    return new DeprecatedRbsReportService(rbsReportRepository, asyncReportGenerationService);
  }

  @Bean
  DeprecatedAsyncRbsReportGenerationService deprecatedAsyncRbsReportGenerationService(
      DeprecatedRbsReportRepository rbsReportRepository,
      DeprecatedRbsReportGenerationService reportGenerationService,
      RbsReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {

    return new DeprecatedAsyncRbsReportGenerationService(
        rbsReportRepository,
        reportGenerationService,
        DefaultTimeSource.INSTANCE,
        properties.getProduction(),
        properties.getSimulation(),
        productionIndexerQuery,
        simulationIndexerQuery);
  }

  @Bean
  DeprecatedRbsReportQuery deprecatedRbsReportQuery(DeprecatedRbsReportRepository repository) {
    return new DeprecatedRbsReportQuery(repository);
  }
}
