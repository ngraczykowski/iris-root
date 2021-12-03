package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.LEARNING_JOB_NAME;

@RequiredArgsConstructor
@Configuration
@Slf4j
class LearningJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final Step storeCsvFileStep;
  private final Step transformAlertStep;
  private final Step transformActionStep;
  private final Step transformHitStep;
  private final Step transformListedRecordStep;
  private final Step deleteFileStep;

  @Bean
  Job learningJob() {
    return this.jobBuilderFactory.get(LEARNING_JOB_NAME)
        .start(storeCsvFileStep)
        .next(transformAlertStep)
        .next(transformActionStep)
        .next(transformHitStep)
        .next(transformListedRecordStep)
        .next(deleteFileStep)
        .build();
  }

  @AfterJob
  void clearDuplicates() {

  }
}
