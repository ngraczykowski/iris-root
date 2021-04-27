package com.silenteight.warehouse.report.synchronization;

import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.storage.ReportStorageService;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableConfigurationProperties(SynchronizationProperties.class)
class SynchronizationConfiguration {

  @Bean
  ReportSynchronizationService reportSynchronizationService(ReportRepository reportRepository) {
    return new ReportSynchronizationService(reportRepository);
  }

  @Bean
  ReportSynchronizationUseCase reportSynchronizationUseCase(
      ReportingService reportingService,
      ReportSynchronizationService reportSynchronizationService,
      ReportStorageService reportStorageService,
      SynchronizationProperties properties) {
    return new ReportSynchronizationUseCase(reportingService, reportSynchronizationService,
        reportStorageService, properties.getProductionTenant());
  }
}
