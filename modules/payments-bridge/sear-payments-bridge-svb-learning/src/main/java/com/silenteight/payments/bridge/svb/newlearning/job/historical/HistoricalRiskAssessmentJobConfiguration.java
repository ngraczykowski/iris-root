package com.silenteight.payments.bridge.svb.newlearning.job.historical;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_RISK_ASSESSMENT_JOB_NAME;

@RequiredArgsConstructor
@Configuration
@Slf4j
class HistoricalRiskAssessmentJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final Step historicalAssessmentReservationStep;

  @Bean
  Job historicalRiskAssessmentJob() {
    return jobBuilderFactory.get(HISTORICAL_RISK_ASSESSMENT_JOB_NAME)
        .start(historicalAssessmentReservationStep)
        .build();
  }
}
