package com.silenteight.scb.ingest.adapter.incomming.common.quartz;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.quartz.QuartzConfiguration.BatchJobConfiguration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
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
  @ConditionalOnProperty(
      prefix = "silenteight.scb-bridge.learning.alert",
      name = "enabled",
      havingValue = "true")
  Trigger scbAlertLevelLearningJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "scbAlertLevelLearningJobTrigger",
        scbAlertLevelLearningJobDetail().getKey(),
        alertLevelLearningJobProperties.getCronExpression());
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "silenteight.scb-bridge.learning.ecm",
      name = "enabled",
      havingValue = "true")
  Trigger ecmLearningJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "ecmLearningJobTrigger",
        ecmLearningJobDetail().getKey(),
        learningEcmJobProperties.getCronExpression());
  }

}
