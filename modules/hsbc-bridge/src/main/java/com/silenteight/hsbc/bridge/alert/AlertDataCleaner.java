package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
class AlertDataCleaner implements DataCleaner {

  private final AlertPayloadRepository payloadRepository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    var cleanedAlertsPayloadCount = payloadRepository.deletePayloadByAlertTimeBefore(expireDate);
    log.debug("Cleaned alerts payload count: {}", cleanedAlertsPayloadCount);
  }
}
