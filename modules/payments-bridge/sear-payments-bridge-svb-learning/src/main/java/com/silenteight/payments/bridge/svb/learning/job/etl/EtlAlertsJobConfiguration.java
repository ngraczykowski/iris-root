package com.silenteight.payments.bridge.svb.learning.job.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.learning.job.etl.EtlJobConstants.ETL_JOB_NAME;

@RequiredArgsConstructor
@Configuration
@Slf4j
class EtlAlertsJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final Step etlAlertStep;
  private final Step etlReservationStep;
  private final EtlAlertsJobListener etlAlertsJobListener;

  @Bean
  Job etlAlertJob() {
    return jobBuilderFactory.get(ETL_JOB_NAME)
        .start(etlReservationStep)
        .next(etlAlertStep)
        .listener(etlAlertsJobListener)
        .build();
  }
}
