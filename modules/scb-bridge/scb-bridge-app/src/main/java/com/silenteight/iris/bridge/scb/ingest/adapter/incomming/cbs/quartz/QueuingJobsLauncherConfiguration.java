/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnQueuingJobsCondition;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class QueuingJobsLauncherConfiguration {

  private final QueuingJobsProperties queuingJobsProperties;
  private final Scheduler scheduler;

  @Bean
  @Conditional(OnQueuingJobsCondition.class)
  QueuingJobsLauncher queuingJobsLauncher() {
    return QueuingJobsLauncher.builder()
        .queuingJobs(queuingJobsProperties.getJobs())
        .scheduler(scheduler)
        .build();
  }
}
