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

  private final DataRetentionProperties properties;
  private final TaskScheduler taskScheduler;
  private final DataRetentionJob dataRetentionJob;
  private final DataRetentionDryRunJob dataRetentionDryRunJob;

  @EventListener(ApplicationStartedEvent.class)
  public void applicationStarted() {
    log.info(
        "Data retention properties: "
            + "dryRunEnabled={}, "
            + "alertsExpiredEnabled={}, "
            + "alertsExpiredDuration={} days, "
            + "personalInformationExpiredEnabled={}, "
            + "personalInformationExpiredDuration={} days",
        properties.getDryRunMode().isEnabled(),
        properties.getAlertsExpired().isEnabled(),
        properties.getAlertsExpired().getDuration().toDays(),
        properties.getPersonalInformationExpired().isEnabled(),
        properties.getPersonalInformationExpired().getDuration().toDays());

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
      schedulePayloadRetentionJob(
          personalInfoExpiredDuration, DataRetentionType.PERSONAL_INFO_EXPIRED);
    } else {
      var alertsExpiredDuration = properties.getAlertsExpired().getDuration();
      schedulePayloadRetentionJob(alertsExpiredDuration, DataRetentionType.ALERTS_EXPIRED);
    }
  }

  private void schedulePayloadRetentionJob(Duration duration, DataRetentionType type) {
    Runnable job;
    if (properties.getDryRunMode().isEnabled()) {
      log.debug("Registering dry run job, day rate={}", properties.getRate().toDays());
      job = () -> dataRetentionDryRunJob.process(duration);
    } else {
      log.debug("Registering payload cleaner job, day rate={}", properties.getRate().toDays());
      var jobProperties = DataRetentionJobProperties.builder()
          .dataRetentionDuration(duration)
          .type(type)
          .chunkSize(properties.getChunk())
          .build();
      job = () -> dataRetentionJob.process(jobProperties);
    }
    taskScheduler.scheduleAtFixedRate(job, properties.getRate());
  }
}
