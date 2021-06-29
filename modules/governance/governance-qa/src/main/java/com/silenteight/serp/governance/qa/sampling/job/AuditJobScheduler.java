package com.silenteight.serp.governance.qa.sampling.job;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
class AuditJobScheduler {

  @NonNull
  private final AlertsGenerator alertsGenerator;

  @Scheduled(cron = "${serp.governance.qa.sampling.schedule.audit-cron}")
  void handleAudit() {
    alertsGenerator.generateAlertsIfNeeded();
  }
}
