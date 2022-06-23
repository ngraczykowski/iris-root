package com.silenteight.serp.governance.qa.sampling.job;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
class AuditJobScheduler {

  @NonNull
  private final AlertsGenerator alertsGenerator;

  @Scheduled(cron = "${serp.governance.qa.sampling.schedule.audit-cron}")
  void handleAudit() {
    try {
      alertsGenerator.generateAlertsIfNeeded();
    } catch (RuntimeException e) {
      log.error("Generating alerts failed.", e);
    }
  }
}
