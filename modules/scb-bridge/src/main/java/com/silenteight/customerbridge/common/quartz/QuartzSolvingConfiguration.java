package com.silenteight.customerbridge.common.quartz;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.quartz.QuartzConfiguration.BatchJobConfiguration;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RequiredArgsConstructor
@Import(JobRegistryConfiguration.class)
class QuartzSolvingConfiguration {

  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;
  private final QuartzConfiguration quartzConfiguration;

  @Bean
  JobDetail scbAlertLevelSolvingJobDetail() {
    return quartzConfiguration.createBatchJobLaunchingJob(
        BatchJobConfiguration.SOLVING_ALERT_LEVEL);
  }

  @Bean
  JobDetail scbWatchlistLevelSolvingJobDetail() {
    return quartzConfiguration.createBatchJobLaunchingJob(
        BatchJobConfiguration.SOLVING_WATCHLIST_LEVEL);
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "serp.scb.bridge.solving.alert.level",
      name = "enabled",
      havingValue = "true")
  Trigger scbAlertLevelSolvingJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "scbAlertLevelSolvingJobTrigger",
        scbAlertLevelSolvingJobDetail().getKey(),
        alertLevelSolvingJobProperties.getCronExpression());
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "serp.scb.bridge.solving.watchlist.level",
      name = "enabled",
      havingValue = "true")
  Trigger scbWatchlistLevelSolvingJobTrigger() {
    return QuartzConfiguration.createCronTrigger(
        "scbWatchlistLevelSolvingJobTrigger",
        scbWatchlistLevelSolvingJobDetail().getKey(),
        watchlistLevelSolvingJobProperties.getCronExpression());
  }
}
