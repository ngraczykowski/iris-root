package com.silenteight.payments.bridge.svb.newlearning.job.contextual;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_RISK_ASSESSMENT_JOB_NAME;

@RequiredArgsConstructor
@Configuration
class ContextualJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final Step contextualReservationStep;
  private final Step storeContextualLearningInLearningEngineStep;

  @Bean
  Job contextualLearningJob() {
    return jobBuilderFactory.get(HISTORICAL_RISK_ASSESSMENT_JOB_NAME)
        .start(contextualReservationStep)
        .next(storeContextualLearningInLearningEngineStep)
        .build();
  }
}
