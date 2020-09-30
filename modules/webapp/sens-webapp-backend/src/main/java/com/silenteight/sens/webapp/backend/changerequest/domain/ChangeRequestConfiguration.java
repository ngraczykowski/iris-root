package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableConfigurationProperties(ChangeRequestProperties.class)
@EnableJpaRepositories
class ChangeRequestConfiguration {

  @Bean
  ChangeRequestQuery changeRequestQuery(
      ChangeRequestRepository repository, ChangeRequestProperties properties) {
    return new ChangeRequestQuery(repository, properties);
  }

  @Bean
  ChangeRequestService changeRequestService(
      ChangeRequestRepository repository, AuditTracer auditTracer) {

    return new ChangeRequestService(repository, auditTracer);
  }
}
