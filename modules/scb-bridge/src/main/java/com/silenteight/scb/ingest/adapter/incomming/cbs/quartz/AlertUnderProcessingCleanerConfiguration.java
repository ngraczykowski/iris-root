package com.silenteight.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.RequiredArgsConstructor;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertUnderProcessingCleanerConfiguration {

  private final AlertUnderProcessingCleanerProperties properties;

  @Bean
  JobDetail alertUnderProcessingCleanerJobDetail() {
    return JobBuilder
        .newJob(AlertUnderProcessingCleaner.class)
        .withIdentity("alertUnderProcessingCleanerJob")
        .storeDurably()
        .build();
  }

  @Bean
  Trigger alertUnderProcessingCleanerTrigger() {
    return TriggerBuilder
        .newTrigger()
        .forJob(alertUnderProcessingCleanerJobDetail().getKey())
        .withSchedule(CronScheduleBuilder.cronSchedule(properties.getCronExpression()))
        .build();
  }
}
