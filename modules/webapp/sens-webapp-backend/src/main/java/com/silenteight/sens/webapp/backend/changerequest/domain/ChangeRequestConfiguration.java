package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ChangeRequestConfiguration {

  @Bean
  ChangeRequestQuery changeRequestQuery(ChangeRequestRepository repository) {
    return new ChangeRequestQuery(repository);
  }

  @Bean
  ChangeRequestService changeRequestService(
      ChangeRequestRepository repository, AuditTracer auditTracer) {

    return new ChangeRequestService(repository, auditTracer);
  }
}
