package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
class BulkDataCleaner implements DataCleaner {

  private final BulkPayloadRepository bulkPayloadRepository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    bulkPayloadRepository.deletePayloadByCreatedAtBefore(expireDate);
  }
}
