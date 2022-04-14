package com.silenteight.hsbc.bridge.retention;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

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
    var expirationDate = getExpireDate();
    log.info("Broadcasting messages has been started with expiration date: {}", expirationDate);

    broadcastMessage(expirationDate);

    log.info("Broadcasting messages has been finished.");

    log.info("\nData cleaning has been started.");

    cleanAlertData(expirationDate);
    cleanMatchData(expirationDate);

    log.info("Data cleaning has been finished.");
  }

  private void broadcastMessage(OffsetDateTime expirationDate) {
    alertRetentionMessageSender.send(expirationDate, chunkSize, type);
  }

  private void cleanAlertData(OffsetDateTime expirationDate) {
    alertDataCleaner.clean(expirationDate);
  }

  private void cleanMatchData(OffsetDateTime expirationDate) {
    matchDataCleaner.clean(expirationDate);
  }

  private OffsetDateTime getExpireDate() {
    return OffsetDateTime.now().minus(dataRetentionDuration);
  }
}
