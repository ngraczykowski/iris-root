package com.silenteight.sens.webapp.backend.changerequest.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ChangeRequestConfiguration {

  @Bean
  ChangeRequestService changeRequestService(ChangeRequestRepository repository) {
    return new ChangeRequestService(repository);
  }
}
