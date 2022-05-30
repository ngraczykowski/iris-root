package com.silenteight.bridge.core.registration.adapter.incoming.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "silenteight.bridge.data-retention.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
class DataRetentionScheduler {

  private final RegistrationFacade registrationFacade;
  private final DataRetentionSchedulerProperties properties;

  @Scheduled(cron = "${silenteight.bridge.data-retention.cron}")
  @SchedulerLock(name = "dataRetention",
      lockAtLeastFor = "${silenteight.bridge.data-retention.lock-at-least-for}")
  void run() {
    log.info(
        "Data retention properties: dryRunEnabled [{}], duration [{}] days, chunkSize [{}]",
        properties.dryRunMode().enabled(), properties.duration().toDays(), properties.chunk());

    var command = StartDataRetentionCommand.builder()
        .duration(properties.duration())
        .chunkSize(properties.chunk())
        .mode(getMode(properties))
        .build();
    registrationFacade.startDataRetention(command);
  }

  private DataRetentionMode getMode(DataRetentionSchedulerProperties properties) {
    return properties.dryRunMode().enabled() ? DataRetentionMode.DRY : DataRetentionMode.WET;
  }

}
