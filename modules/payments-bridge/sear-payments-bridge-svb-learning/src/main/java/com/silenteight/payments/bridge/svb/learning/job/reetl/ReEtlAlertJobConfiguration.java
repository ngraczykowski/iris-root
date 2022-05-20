package com.silenteight.payments.bridge.svb.learning.job.reetl;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.learning.job.reetl.ReEtlJobConstants.RE_ETL_JOB_NAME;

@Configuration
public class ReEtlAlertJobConfiguration {


  @Bean
  Job reEtlAlertJob(
      final Step readCompositeAlertStep,
      final JobBuilderFactory jobBuilderFactory
  ) {
    return jobBuilderFactory.get(RE_ETL_JOB_NAME)
        .start(readCompositeAlertStep)
        .build();
  }
}
