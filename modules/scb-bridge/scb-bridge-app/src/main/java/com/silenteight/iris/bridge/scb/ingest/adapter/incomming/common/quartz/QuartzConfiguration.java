/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.BatchConstants.JobName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;

import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@Configuration
@Import(JobRegistryConfiguration.class)
class QuartzConfiguration {

  private static final String TRIGGER_IDENTITY_PREFIX =
      QuartzConfiguration.class.getPackageName() + ":trigger#";
  private static final String JOB_IDENTITY_PREFIX =
      QuartzConfiguration.class.getPackageName() + ":job#";

  private final JobLauncher jobLauncher;
  private final JobLocator jobLocator;
  private final JobOperator jobOperator;

  JobDataMap createCommonJobDataMap(BatchJobConfiguration jobConfiguration) {
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("jobName", jobConfiguration.getJobName());
    jobDataMap.put("jobLauncher", jobLauncher);
    jobDataMap.put("jobLocator", jobLocator);
    jobDataMap.put("jobOperator", jobOperator);
    jobDataMap.put(GnsSyncConstants.GNS_SYNC_MODE_KEY, jobConfiguration.getJobType());

    return jobDataMap;
  }

  JobDetail createBatchJobLaunchingJob(BatchJobConfiguration jobConfiguration) {
    JobDataMap jobDataMap = createCommonJobDataMap(jobConfiguration);

    return JobBuilder
        .newJob(QuartzBatchJobLauncher.class)
        .withIdentity(JOB_IDENTITY_PREFIX + jobConfiguration.getIdentity())
        .setJobData(jobDataMap)
        .storeDurably()
        .build();
  }

  static Trigger createCronTrigger(
      String identity, JobKey jobKey, String cronExpression) {

    return TriggerBuilder
        .newTrigger()
        .forJob(jobKey)
        .withIdentity(TRIGGER_IDENTITY_PREFIX + identity)
        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
        .build();
  }

  @Getter
  @AllArgsConstructor
  enum BatchJobConfiguration {
    SCB_ALERT_LEVEL_LEARNING(
        JobName.SCB_ALERT_LEVEL_LEARNING_SYNC, "scbAlertLevelLearningJob", "LEARNING_AL"),
    LEARNING_ECM(
        JobName.ECM_LEARNING_SYNC, "ecmLearningJob", "ECM_LEARNING");

    private String jobName;
    private String identity;
    private String jobType;
  }
}
