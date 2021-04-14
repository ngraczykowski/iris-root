package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
class AlertDataCleaner implements DataCleaner {

  private final AlertRepository repository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    repository.deletePayloadByCreatedAtBefore(expireDate);
  }
}
