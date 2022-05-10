package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnLearningAlertCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnLearningEcmCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.QuartzConfiguration.BatchJobConfiguration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RequiredArgsConstructor
@Import(JobRegistryConfiguration.class)
class QuartzLearningConfiguration {

  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties learningEcmJobProperties;
  private final QuartzConfiguration quartzConfiguration;

  @Bean
  JobDetail scbAlertLevelLearningJobDetail() {
    return quartzConfiguration.createBatchJobLaunchingJob(
        BatchJobConfiguration.SCB_ALERT_LEVEL_LEARNING);
  }

  @Bean
  JobDetail ecmLearningJobDetail() {
    return quartzConfiguration.createBatchJobLaunchingJob(BatchJobConfiguration.LEARNING_ECM);
  }

  @Bean
  @Conditional(OnLearningAlertCondition.class)
  Trigger scbAlertLevelLearningJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "scbAlertLevelLearningJobTrigger",
        scbAlertLevelLearningJobDetail().getKey(),
        alertLevelLearningJobProperties.getCronExpression());
  }

  @Bean
  @Conditional(OnLearningEcmCondition.class)
  Trigger ecmLearningJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "ecmLearningJobTrigger",
        ecmLearningJobDetail().getKey(),
        learningEcmJobProperties.getCronExpression());
  }

}
