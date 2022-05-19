package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
    value = "silenteight.scb-bridge.retention.raw-alerts.enabled", havingValue = "true")
class RawAlertsDataRetentionJob {

  private final RawAlertsDataRetentionProperties retentionProperties;
  private final RawAlertRepository rawAlertRepository;

  @Scheduled(cron = "@monthly")
  void removeExpiredAlerts() {
    var expiredDate = getExpiredDate(retentionProperties.expiredAfter());
    rawAlertRepository.removeExpiredPartitions(expiredDate);
  }

  OffsetDateTime getExpiredDate(Duration expiredAfter) {
    return OffsetDateTime.now().minus(expiredAfter);
  }
}
