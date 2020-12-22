package com.silenteight.serp.governance.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ModelConfiguration {

  @Bean
  ModelActivator modelActivator(ModelRepository modelRepository) {
    return new ModelActivator(modelRepository);
  }

  @Bean
  ModelService modelService(ModelRepository modelRepository,
                            ModelDifferenceCalculator modelDifferenceCalculator,
                            ModelActivator modelActivator) {
    return new ModelService(modelRepository, modelDifferenceCalculator, modelActivator);
  }

  @Bean
  ModelDifferenceCalculator modelDifferenceCalculator() {
    return new ModelDifferenceCalculator();
  }

  @Bean
  ModelFinder modelFinder(ModelRepository modelRepository) {
    return new ModelFinder(modelRepository);
  }

  InMemoryModelRepository inMemoryRepository() {
    return new InMemoryModelRepository();
  }

}
