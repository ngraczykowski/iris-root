package com.silenteight.payments.bridge.app.scheduler;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.port.CheckAlertExpirationUseCase;
import com.silenteight.payments.bridge.data.retention.port.CheckPersonalInformationExpirationUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DataRetentionScheduler {

  private final CheckAlertExpirationUseCase checkAlertExpirationUseCase;
  private final CheckPersonalInformationExpirationUseCase checkPersonalInformationExpirationUseCase;

  @Scheduled(cron = "${pb.data-retention.alert-data.cron}")
  @SchedulerLock(name = "alert_data_cron_lock", lockAtMostFor = "3600")
  void scheduleAlertValidator() {
    checkAlertExpirationUseCase.execute();
  }

  @Scheduled(cron = "${pb.data-retention.personal-information.cron}")
  @SchedulerLock(name = "pii_cron_lock", lockAtMostFor = "3600")
  void schedulePersonalInformationValidator() {
    checkPersonalInformationExpirationUseCase.execute();
  }
}
