package com.silenteight.hsbc.bridge.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
class DataRetentionDryRunJob {

  private final DryRunDataCleaner dryRunDataCleaner;
  private final DryRunJobRepository jobRepository;
  private final DryRunJobAlertRepository alertRepository;

  @Transactional
  public void process(Duration dataRetentionDuration) {
    log.info("Started data retention process in dry run");

    var expirationDate = getExpirationDate(dataRetentionDuration);
    var jobId = jobRepository.save(expirationDate);
    var alertNames = dryRunDataCleaner.getAlertNamesToClean(expirationDate);
    log.info(
        "Found {} alerts to purge in job with id {}, expirationDate: {}", alertNames.size(), jobId,
        expirationDate);
    if (CollectionUtils.isNotEmpty(alertNames)) {
      alertRepository.saveAll(jobId, alertNames);
    }

    log.info("Finished processing job with id {}", jobId);
  }

  private OffsetDateTime getExpirationDate(Duration dataRetentionDuration) {
    return OffsetDateTime.now().minus(dataRetentionDuration);
  }
}
