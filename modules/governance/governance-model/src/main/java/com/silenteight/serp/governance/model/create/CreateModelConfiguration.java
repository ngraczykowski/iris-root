package com.silenteight.serp.governance.model.create;

import com.silenteight.serp.governance.model.domain.ModelService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateModelConfiguration {

  @Bean
  CreateModelUseCase createModelUseCase(ModelService modelService) {
    return new CreateModelUseCase(modelService);
  }
}
