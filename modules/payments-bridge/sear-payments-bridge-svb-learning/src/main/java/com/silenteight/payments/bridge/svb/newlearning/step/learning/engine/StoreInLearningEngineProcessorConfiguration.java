package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.migration.DecisionMapper;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC;

@Configuration
@RequiredArgsConstructor
class StoreInLearningEngineProcessorConfiguration {

  @Bean
  @StepScope
  StoreInLearningEngineProcessor storeInLearningEngineProcessor(
      final DecisionMapper decisionMapper) {
    return new StoreInLearningEngineProcessor(
        decisionMapper, HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC);
  }

  @Bean
  @StepScope
  StoreInLearningEngineProcessor storeInLearningEngineContextualProcessor(
      final DecisionMapper decisionMapper) {
    return new StoreInLearningEngineProcessor(decisionMapper, CONTEXTUAL_LEARNING_DISC);
  }
}
