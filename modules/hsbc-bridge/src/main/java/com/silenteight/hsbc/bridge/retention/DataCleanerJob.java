package com.silenteight.hsbc.bridge.retention;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;

@Builder
@Slf4j
class DataCleanerJob {

  private final DataCleaner alertDataCleaner;
  private final DataCleaner bulkDataCleaner;
  private final DataCleaner matchDataCleaner;
  private final Duration dataRetentionDuration;

  @Transactional
  void clean() {
    log.info("Data cleaning has been started.");

    cleanBulkData();
    cleanAlertData();
    cleanMatchData();

    log.info("Data cleaning has been finished.");
  }

  private void cleanMatchData() {
    matchDataCleaner.clean(getExpireDate());
  }

  private void cleanAlertData() {
    alertDataCleaner.clean(getExpireDate());
  }

  private void cleanBulkData() {
    bulkDataCleaner.clean(getExpireDate());
  }

  private OffsetDateTime getExpireDate() {
    return now().minus(dataRetentionDuration);
  }
}
