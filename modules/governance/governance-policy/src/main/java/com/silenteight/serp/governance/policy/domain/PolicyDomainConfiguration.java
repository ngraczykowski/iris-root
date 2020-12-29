package com.silenteight.serp.governance.policy.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class PolicyDomainConfiguration {

  @Bean
  PolicyService policyService(PolicyRepository repository) {
    return new PolicyService(repository);
  }
}
