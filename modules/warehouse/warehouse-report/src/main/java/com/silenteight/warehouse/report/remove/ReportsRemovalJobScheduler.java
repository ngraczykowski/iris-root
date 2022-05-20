package com.silenteight.warehouse.report.remove;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
class ReportsRemovalJobScheduler {

  @NonNull
  ReportsRemovalService reportsRemovalService;

  @Scheduled(cron = "${warehouse.report.removal.cron}")
  void handleReportsRemove() {
    try {
      reportsRemovalService.removeReports();
    } catch (RuntimeException e) {
      log.error("Removing alerts failed.", e);
    }
  }
}
