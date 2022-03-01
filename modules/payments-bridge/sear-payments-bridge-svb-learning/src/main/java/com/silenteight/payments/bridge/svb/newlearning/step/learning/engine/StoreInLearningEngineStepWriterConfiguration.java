package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.port.HistoricalDecisionLearningEnginePort;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class StoreInLearningEngineStepWriterConfiguration {

  private final HistoricalDecisionLearningEnginePort learningEngineBridge;

  @Bean
  @StepScope
  StoreInLearningEngineWriter storeInLearningEngineWriter() {
    return new StoreInLearningEngineWriter(learningEngineBridge);
  }

}
