package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class RbsReportConfiguration {

  @Bean
  RbsReportService rbsReportService(
      RbsReportRepository rbsReportRepository,
      AsyncRbsReportGenerationService asyncReportGenerationService) {
    return new RbsReportService(rbsReportRepository, asyncReportGenerationService);
  }

  @Bean
  AsyncRbsReportGenerationService asyncRbsReportGenerationService(
      RbsReportRepository rbsReportRepository,
      RbsReportGenerationService reportGenerationService,
      RbsReportProperties properties,
      @Qualifier(value = "productionIndexingQuery") IndexesQuery productionIndexerQuery,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery simulationIndexerQuery) {
    return new AsyncRbsReportGenerationService(
        rbsReportRepository,
        reportGenerationService,
        DefaultTimeSource.INSTANCE,
        properties.getProduction(),
        properties.getSimulation(),
        productionIndexerQuery,
        simulationIndexerQuery);
  }

  @Bean
  RbsReportQuery rbsReportQuery(RbsReportRepository repository) {
    return new RbsReportQuery(repository);
  }
}
