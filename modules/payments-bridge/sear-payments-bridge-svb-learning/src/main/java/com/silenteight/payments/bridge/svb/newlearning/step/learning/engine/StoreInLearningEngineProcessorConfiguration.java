package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.migration.DecisionMapper;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.*;

@Configuration
@RequiredArgsConstructor
class StoreInLearningEngineProcessorConfiguration {

  @Bean
  @StepScope
  StoreInLearningEngineProcessor storeInLearningEngineProcessor(
      final DecisionMapper decisionMapper) {
    return new StoreInLearningEngineProcessor(
        decisionMapper, List.of(
        HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP,
        HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP));
  }

  @Bean
  @StepScope
  StoreInLearningEngineProcessor storeInLearningEngineContextualProcessor(
      final DecisionMapper decisionMapper) {
    return new StoreInLearningEngineProcessor(
        decisionMapper, List.of(CONTEXTUAL_LEARNING_DISC_FP, CONTEXTUAL_LEARNING_DISC_TP));
  }
}
