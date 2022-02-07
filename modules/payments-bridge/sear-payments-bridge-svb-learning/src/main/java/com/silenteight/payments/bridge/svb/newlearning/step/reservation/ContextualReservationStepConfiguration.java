package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.CONTEXTUAL_LEARNING_RESERVATION_STEP;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(AlertReservationProperties.class)
class ContextualReservationStepConfiguration {

  @Bean
  Step contextualReservationStep(
      final StepBuilderFactory stepBuilderFactory,
      final Tasklet contextualReservationTasklet) {
    return stepBuilderFactory
        .get(CONTEXTUAL_LEARNING_RESERVATION_STEP)
        .tasklet(contextualReservationTasklet)
        .build();
  }
}
