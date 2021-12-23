package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class StoreInLearningEngineProcessorConfiguration {

  @Bean
  @StepScope
  StoreInLearningEngineProcessor storeInLearningEngineProcessor() {
    return new StoreInLearningEngineProcessor();
  }
}
