package com.silenteight.hsbc.bridge.retention;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;

@Builder
@Slf4j
class DataRetentionJob {

  private final DataCleaner alertDataCleaner;
  private final DataCleaner matchDataCleaner;
  private final AlertRetentionSender alertRetentionMessageSender;
  private final Duration dataRetentionDuration;
  private final DataRetentionType type;
  private final int chunkSize;

  @Transactional
  void process() {
    log.info("Broadcasting messages has been started.");

    broadcastMessage();

    log.info("Broadcasting messages has been finished.");

    log.info("\nData cleaning has been started.");

    cleanAlertData();
    cleanMatchData();

    log.info("Data cleaning has been finished.");
  }

  private void broadcastMessage() {
    alertRetentionMessageSender.send(getExpireDate(), chunkSize, type);
  }

  private void cleanAlertData() {
    alertDataCleaner.clean(getExpireDate());
  }

  private void cleanMatchData() {
    matchDataCleaner.clean(getExpireDate());
  }

  private OffsetDateTime getExpireDate() {
    return now().minus(dataRetentionDuration);
  }
}
