package com.silenteight.warehouse.report.synchronization;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class SynchronizationConfiguration {

  @Bean
  ReportSynchronizationService reportSynchronizationService(ReportRepository reportRepository) {
    return new ReportSynchronizationService(reportRepository);
  }
}
