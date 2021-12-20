package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_RESERVATION_STEP;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(AlertReservationProperties.class)
class HistoricalAssessmentReservationStepConfiguration {

  @Language("PostgreSQL")
  private static final String WRITER_INSERT = "INSERT INTO pb_learning_historical_reservation"
      + " VALUES(?,?)";

  private final AlertReservationStepFactory alertReservationStepFactory;
  private final AlertReservationProperties properties;

  @Bean
  Step historicalAssessmentReservationStep() {
    return alertReservationStepFactory.createStep(
        HISTORICAL_ASSESSMENT_RESERVATION_STEP,
        WRITER_INSERT,
        properties.getChunkSize());
  }


}
