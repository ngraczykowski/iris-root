package com.silenteight.sens.webapp.backend.report.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ReportDomainConfiguration {

  @Bean
  ReportMetadataService reportMetadataService(ReportMetadataRepository repository) {
    return new ReportMetadataService(repository);
  }
}
