package com.silenteight.simulator.dataset.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class DatasetConfiguration {

  @Bean
  DatasetMetadataService datasetMetadataService(DatasetEntityRepository repository) {
    return new DatasetMetadataService(repository);
  }

  @Bean
  DatasetQuery datasetQuery(DatasetEntityRepository repository) {
    return new DatasetQuery(repository);
  }
}
