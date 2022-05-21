package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DryRunDataCleaner;

import java.time.OffsetDateTime;
import java.util.Set;

@RequiredArgsConstructor
class AlertDryRunDataCleaner implements DryRunDataCleaner {

  private final AlertPayloadRepository payloadRepository;

  @Override
  public Set<String> getAlertNamesToClean(OffsetDateTime expireDate) {
    return payloadRepository.getAlertNamesByAlertTimeBefore(expireDate);
  }
}
