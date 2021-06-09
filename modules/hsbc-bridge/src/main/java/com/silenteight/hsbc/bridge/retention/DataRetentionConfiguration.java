package com.silenteight.hsbc.bridge.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@EnableConfigurationProperties(DataRetentionProperties.class)
@RequiredArgsConstructor
@Slf4j
class DataRetentionConfiguration {

  private final DataCleaner alertDataCleaner;
  private final DataCleaner matchDataCleaner;

  private final DataRetentionProperties properties;
  private final TaskScheduler taskScheduler;

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    if (properties.isEnabled()) {
      schedulePayloadCleanerJob();
    }
  }

  private void schedulePayloadCleanerJob() {
    log.debug("Registering payload cleaner job, rate={}", properties.getRate());

    taskScheduler.scheduleAtFixedRate(getPayloadCleanerJob()::clean, properties.getRate());
  }

  private DataCleanerJob getPayloadCleanerJob() {
    return DataCleanerJob.builder()
        .alertDataCleaner(alertDataCleaner)
        .matchDataCleaner(matchDataCleaner)
        .dataRetentionDuration(properties.getDuration())
        .build();
  }
}
