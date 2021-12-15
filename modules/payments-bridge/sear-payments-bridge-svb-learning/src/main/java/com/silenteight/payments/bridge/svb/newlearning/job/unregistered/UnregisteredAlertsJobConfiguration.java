package com.silenteight.payments.bridge.svb.newlearning.job.unregistered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.step.unregistered.UnregisteredJobConstants.UNREGISTERED_JOB_NAME;

@RequiredArgsConstructor
@Configuration
@Slf4j
class UnregisteredAlertsJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final Step processUnregisterAlertStep;

  @Bean
  Job processUnregisteredAlertJob() {
    return jobBuilderFactory.get(UNREGISTERED_JOB_NAME)
        .start(processUnregisterAlertStep)
        .build();
  }
}
