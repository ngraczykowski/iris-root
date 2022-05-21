package com.silenteight.hsbc.bridge.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
class DataRetentionJob {

  private final DataCleaner alertDataCleaner;
  private final DataCleaner matchDataCleaner;
  private final AlertRetentionSender alertRetentionMessageSender;

  @Transactional
  public void process(DataRetentionJobProperties properties) {
    log.info("Started data retention process");

    var expirationDate = getExpirationDate(properties);
    log.info("Broadcasting messages has been started with expiration date: {}", expirationDate);

    broadcastMessage(expirationDate, properties);

    log.info("Broadcasting messages has been finished.");

    log.info("\nData cleaning has been started.");

    cleanAlertData(expirationDate);
    cleanMatchData(expirationDate);

    log.info("Data cleaning has been finished.");
  }

  private void broadcastMessage(
      OffsetDateTime expirationDate, DataRetentionJobProperties properties) {
    alertRetentionMessageSender.send(
        expirationDate, properties.getChunkSize(), properties.getType());
  }

  private void cleanAlertData(OffsetDateTime expirationDate) {
    alertDataCleaner.clean(expirationDate);
  }

  private void cleanMatchData(OffsetDateTime expirationDate) {
    matchDataCleaner.clean(expirationDate);
  }

  private OffsetDateTime getExpirationDate(DataRetentionJobProperties properties) {
    return OffsetDateTime.now().minus(properties.getDataRetentionDuration());
  }
}
