package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ChangeRequestDomainConfiguration {

  @Bean
  ChangeRequestService changeRequestService(
      ChangeRequestRepository changeRequestRepository, AuditingLogger auditingLogger) {

    return new ChangeRequestService(changeRequestRepository, auditingLogger);
  }

  @Bean
  ChangeRequestQuery changeRequestQuery(ChangeRequestRepository changeRequestRepository) {
    return new ChangeRequestQuery(changeRequestRepository);
  }
}
