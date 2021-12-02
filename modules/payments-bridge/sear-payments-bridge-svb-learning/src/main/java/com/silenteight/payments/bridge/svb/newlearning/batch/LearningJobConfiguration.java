package com.silenteight.payments.bridge.svb.newlearning.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
class LearningJobConfiguration {

  static final String JOB_NAME = "learning-job";

  private final JobBuilderFactory jobBuilderFactory;

  private final Step storeCsvFileStep;
  private final Step transformAlertStep;
  private final Step transformActionStep;
  private final Step transformHitStep;
  private final Step transformListedRecordStep;
  private final Step transformStatusStep;
  private final Step deleteFileStep;
  private final Step transformAlertedMessageStep;

  @Bean("svbCsvJob")
  Job svbLearningJob() {
    return this.jobBuilderFactory.get(JOB_NAME)
        .start(storeCsvFileStep)
        .next(transformAlertStep)
        .next(transformActionStep)
        .next(transformHitStep)
        .next(transformListedRecordStep)
        .next(transformStatusStep)
        .next(transformAlertedMessageStep)
        .next(deleteFileStep)
        .build();
  }


}
