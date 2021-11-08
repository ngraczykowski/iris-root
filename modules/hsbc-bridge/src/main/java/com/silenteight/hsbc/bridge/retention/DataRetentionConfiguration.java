package com.silenteight.hsbc.bridge.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(DataRetentionProperties.class)
@RequiredArgsConstructor
@Slf4j
class DataRetentionConfiguration {

  private final AlertRetentionSender alertRetentionMessageSender;
  private final DataCleaner alertDataCleaner;
  private final DataCleaner matchDataCleaner;

  private final DataRetentionProperties properties;
  private final TaskScheduler taskScheduler;

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    var isPersonalInfoExpired = properties.getPersonalInformationExpired().isEnabled();
    var isAlertsExpired = properties.getAlertsExpired().isEnabled();
    if (isPersonalInfoExpired ^ isAlertsExpired) {
      schedulePayloadRetentionJobForGivenEnvironment(isPersonalInfoExpired);
    } else {
      log.warn("Set proper environment variable in application.yaml for data retention!");
    }
  }

  private void schedulePayloadRetentionJobForGivenEnvironment(boolean isPersonalInfoExpired) {
    if (isPersonalInfoExpired) {
      var personalInfoExpiredDuration = properties.getPersonalInformationExpired().getDuration();
      schedulePayloadRetentionJob(personalInfoExpiredDuration, DataRetentionType.PERSONAL_INFO_EXPIRED);
    } else {
      var alertsExpiredDuration = properties.getAlertsExpired().getDuration();
      schedulePayloadRetentionJob(alertsExpiredDuration, DataRetentionType.ALERTS_EXPIRED);
    }
  }

  private void schedulePayloadRetentionJob(Duration duration, DataRetentionType type) {
    log.debug("Registering payload cleaner job, rate={}", properties.getRate());

    taskScheduler.scheduleAtFixedRate(
        getPayloadRetentionJob(duration, type)::process, properties.getRate());
  }

  private DataRetentionJob getPayloadRetentionJob(Duration duration, DataRetentionType type) {
    return DataRetentionJob.builder()
        .alertDataCleaner(alertDataCleaner)
        .matchDataCleaner(matchDataCleaner)
        .alertRetentionMessageSender(alertRetentionMessageSender)
        .dataRetentionDuration(duration)
        .type(type)
        .chunkSize(properties.getChunk())
        .build();
  }
}
