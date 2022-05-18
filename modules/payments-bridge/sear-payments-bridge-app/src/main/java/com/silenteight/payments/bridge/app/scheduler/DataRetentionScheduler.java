package com.silenteight.payments.bridge.app.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.data.retention.port.CheckAlertExpirationUseCase;
import com.silenteight.payments.bridge.data.retention.port.CheckFileExpirationUseCase;
import com.silenteight.payments.bridge.data.retention.port.CheckPersonalInformationExpirationUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class DataRetentionScheduler {

  private final CheckAlertExpirationUseCase checkAlertExpirationUseCase;
  private final CheckPersonalInformationExpirationUseCase checkPersonalInformationExpirationUseCase;
  private final CheckFileExpirationUseCase fileExpirationUseCase;

  @Scheduled(cron = "${pb.data-retention.alert-data.cron:*/60 * * * * *}")
  @SchedulerLock(name = "alert_data_cron_lock", lockAtMostFor = "3600")
  void scheduleAlertValidator() {
    log.info("The data-retention scheduler has started looking for expired alert-data");
    checkAlertExpirationUseCase.execute();
  }

  @Scheduled(cron = "${pb.data-retention.personal-information.cron:*/60 * * * * *}")
  @SchedulerLock(name = "pii_cron_lock", lockAtMostFor = "3600")
  void schedulePersonalInformationValidator() {
    log.info("The data-retention scheduler has started looking for "
        + "expired personal identifiable information data.");
    checkPersonalInformationExpirationUseCase.execute();
  }

  @Scheduled(cron = "${pb.data-retention.file.cron:*/60 * * * * *}")
  @SchedulerLock(name = "file_cron_lock", lockAtMostFor = "3600")
  void scheduleFileRetention() {
    log.info("The data-retention scheduler has started looking for expired files.");
    fileExpirationUseCase.execute();
  }
}
