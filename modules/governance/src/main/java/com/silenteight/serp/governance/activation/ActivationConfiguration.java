package com.silenteight.serp.governance.activation;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@RequiredArgsConstructor
class ActivationConfiguration {

  private final ActivationRepository activationRepository;

  @Bean
  ActivationService activationService() {
    return new ActivationService(activationRepository);
  }


}
