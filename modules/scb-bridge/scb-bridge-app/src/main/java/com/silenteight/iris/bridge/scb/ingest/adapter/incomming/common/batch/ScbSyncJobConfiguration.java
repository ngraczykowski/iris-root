/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.BatchConstants.JobName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncService;
import com.silenteight.sep.base.common.batch.DefaultBatchConfigurerConfiguration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Import(DefaultBatchConfigurerConfiguration.class)
class ScbSyncJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final GnsSyncService gnsSyncService;

  private final Step startNewGnsSync;
  private final Step collectRecordsAlertLevelLearning;
  private final Step collectEcmRecordsLearning;
  private final Step finishGnsSyncTasklet;

  @Bean
  Job scbAlertLevelLearningJob() {
    return createSyncBatchJob(
        JobName.SCB_ALERT_LEVEL_LEARNING_SYNC, collectRecordsAlertLevelLearning);
  }

  @Bean
  Job ecmLearningJob() {
    return createSyncBatchJob(JobName.ECM_LEARNING_SYNC, collectEcmRecordsLearning);
  }

  private Job createSyncBatchJob(String jobName, Step collectRecordsStep) {
    return jobBuilderFactory
        .get(jobName)
        .incrementer(new RunIdIncrementer())
        .start(startNewGnsSync)
        .next(collectRecordsStep)
        .next(finishGnsSyncTasklet)
        .listener(new MarkFailedSyncAsErrorListener(gnsSyncService))
        .build();
  }
}
