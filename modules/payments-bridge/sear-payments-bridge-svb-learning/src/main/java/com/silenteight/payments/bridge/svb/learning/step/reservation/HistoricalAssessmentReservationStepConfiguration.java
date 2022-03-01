package com.silenteight.payments.bridge.svb.learning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_RESERVATION_STEP;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(AlertReservationProperties.class)
class HistoricalAssessmentReservationStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final Tasklet historicalAssessmentReservationTasklet;

  @Bean
  Step historicalAssessmentReservationStep() {
    return stepBuilderFactory
        .get(HISTORICAL_ASSESSMENT_RESERVATION_STEP)
        .tasklet(historicalAssessmentReservationTasklet)
        .build();
  }
}
