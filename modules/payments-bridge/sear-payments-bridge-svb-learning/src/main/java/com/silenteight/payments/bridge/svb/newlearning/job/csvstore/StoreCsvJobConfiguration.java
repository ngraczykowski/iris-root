package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STORE_CSV_JOB_NAME;

@RequiredArgsConstructor
@Configuration
@Slf4j
class StoreCsvJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final Step storeCsvFileStep;
  private final Step transformAlertStep;
  private final Step transformActionStep;
  private final Step transformHitStep;
  private final Step removeDuplicatesStep;
  private final Step deleteFileStep;
  private final StoreCsvJobListener storeCsvJobListener;

  @Bean
  Job storeCsvJob() {
    return this.jobBuilderFactory.get(STORE_CSV_JOB_NAME)
        .start(storeCsvFileStep)
        .next(transformAlertStep)
        .next(transformActionStep)
        .next(transformHitStep)
        .next(removeDuplicatesStep)
        .next(deleteFileStep)
        .listener(storeCsvJobListener)
        .build();
  }
}
