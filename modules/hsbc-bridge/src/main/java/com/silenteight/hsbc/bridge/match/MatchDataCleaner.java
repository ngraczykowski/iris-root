package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
class MatchDataCleaner implements DataCleaner {

  private final MatchPayloadRepository payloadRepository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    log.info(
        "Cleaning matches payload for matches with alert_time before (exclusive): {}", expireDate);
    var cleanedMatchesPayloadCount = payloadRepository.deletePayloadByAlertTimeBefore(expireDate);
    log.debug("Cleaned matches payload count: {}", cleanedMatchesPayloadCount);
  }
}
