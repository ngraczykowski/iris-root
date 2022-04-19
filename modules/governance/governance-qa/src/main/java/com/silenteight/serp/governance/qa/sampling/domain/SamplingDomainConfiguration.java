package com.silenteight.serp.governance.qa.sampling.domain;

import com.silenteight.sep.base.common.time.DefaultTimeSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
public class SamplingDomainConfiguration {

  @Bean
  AlertSamplingByStateQuery alertSamplingQuery(AlertSamplingRepository alertSamplingRepository) {
    return new AlertSamplingByStateQuery(alertSamplingRepository);
  }

  @Bean
  AlertSamplingService alertSamplingService(AlertSamplingRepository alertSamplingRepository) {
    return new AlertSamplingService(alertSamplingRepository, DefaultTimeSource.INSTANCE);
  }
}
