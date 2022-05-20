package com.silenteight.warehouse.report.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
class ReportPersistenceConfiguration {

  @Bean
  PersistenceService reportService(ReportRepository reportRepository) {
    return new PersistenceService(reportRepository);
  }
}
