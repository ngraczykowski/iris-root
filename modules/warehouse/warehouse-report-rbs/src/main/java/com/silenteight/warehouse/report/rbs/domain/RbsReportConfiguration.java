package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;

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
      RbsReportRepository rbsReportRepository, RbsReportGenerationService reportGenerationService) {
    return new AsyncRbsReportGenerationService(
        rbsReportRepository, reportGenerationService, DefaultTimeSource.INSTANCE);
  }

  @Bean
  RbsReportQuery rbsReportQuery(RbsReportRepository repository) {
    return new RbsReportQuery(repository);
  }
}
