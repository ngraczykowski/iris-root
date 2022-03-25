package com.silenteight.connector.ftcc.callback.handler;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
class BatchCompletedConfiguration {

  @Bean
  BatchCompletedService batchCompletedService(BatchCompletedRepository batchCompletedRepository) {
    return new BatchCompletedService(batchCompletedRepository);
  }
}
