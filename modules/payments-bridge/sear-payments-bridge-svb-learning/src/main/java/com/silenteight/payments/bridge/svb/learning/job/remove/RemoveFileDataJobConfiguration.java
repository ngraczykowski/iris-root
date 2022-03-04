package com.silenteight.payments.bridge.svb.learning.job.remove;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_FILE_DATA_JOB_NAME;

@RequiredArgsConstructor
@Configuration
class RemoveFileDataJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final Step removeCsvRowsStep;
  private final Step removeAlertsStep;
  private final Step removeHitsStep;

  @Bean
  Job removeFileDataJob() {
    return this.jobBuilderFactory.get(REMOVE_FILE_DATA_JOB_NAME)
        .start(removeCsvRowsStep)
        .next(removeAlertsStep)
        .next(removeHitsStep)
        .build();
  }
}
