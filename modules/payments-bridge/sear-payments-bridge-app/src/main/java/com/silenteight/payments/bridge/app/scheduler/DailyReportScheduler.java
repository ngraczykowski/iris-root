package com.silenteight.payments.bridge.app.scheduler;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.reports.ProcessRemoveDailyReportsUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DailyReportScheduler {

  private final ProcessRemoveDailyReportsUseCase processRemoveDailyReportsUseCase;

  @Scheduled(cron = "${pb.daily-reports-remove.cron:*/600 * * * * *}")
  @SchedulerLock(name = "daily_reports_remove_sending_lock", lockAtMostFor = "3600")
  void scheduleRemoveDailyReports() {
    processRemoveDailyReportsUseCase.processRemoveDailyReports();
  }
}
