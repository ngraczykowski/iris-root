package com.silenteight.bridge.core.registration.adapter.incoming.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;
import com.silenteight.bridge.core.registration.infrastructure.retention.DataRetentionProperties;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED;
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED_DRY_RUN;
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED;
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED_DRY_RUN;

@Component
@ConditionalOnProperty(name = "silenteight.bridge.data-retention.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
class DataRetentionScheduler {

  private final RegistrationFacade registrationFacade;
  private final DataRetentionProperties properties;

  @Scheduled(fixedRateString = "${silenteight.bridge.data-retention.rate}")
  @SchedulerLock(name = "dataRetention", lockAtMostFor = "5m", lockAtLeastFor = "1m")
  void run() {
    log.info(
        """
            Data retention properties:
            dryRunEnabled [{}],
            alertsExpiredEnabled [{}],
            alertsExpiredDuration [{}] days,
            personalInformationExpiredEnabled [{}],
            personalInformationExpiredDuration [{}] days""",
        properties.dryRunMode().enabled(),
        properties.alertsExpired().enabled(),
        properties.alertsExpired().duration().toDays(),
        properties.personalInformationExpired().enabled(),
        properties.personalInformationExpired().duration().toDays());

    var isPersonalInfoExpired = properties.personalInformationExpired().enabled();
    var isAlertsExpired = properties.alertsExpired().enabled();

    if (isPersonalInfoExpired ^ isAlertsExpired) {
      var command = StartDataRetentionCommand.builder()
          .type(getType(isPersonalInfoExpired))
          .duration(getDuration(isPersonalInfoExpired))
          .chunkSize(properties.chunk())
          .build();
      registrationFacade.startDataRetention(command);
    } else {
      log.warn("Either personalInformationExpired or alertsExpired has to be enabled. Not both.");
    }
  }

  private DataRetentionType getType(boolean isPersonalInfoExpired) {
    if (properties.dryRunMode().enabled()) {
      return isPersonalInfoExpired ? PERSONAL_INFO_EXPIRED_DRY_RUN : ALERTS_EXPIRED_DRY_RUN;
    }
    return isPersonalInfoExpired ? PERSONAL_INFO_EXPIRED : ALERTS_EXPIRED;
  }

  private Duration getDuration(boolean isPersonalInfoExpired) {
    return isPersonalInfoExpired ? properties.personalInformationExpired().duration()
                                 : properties.alertsExpired().duration();
  }
}
