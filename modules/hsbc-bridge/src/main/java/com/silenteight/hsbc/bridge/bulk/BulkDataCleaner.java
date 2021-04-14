package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
class BulkDataCleaner implements DataCleaner {

  private final BulkItemRepository bulkItemRepository;

  @Override
  @Transactional
  public void clean(OffsetDateTime expireDate) {
    bulkItemRepository.deletePayloadByCreatedAtBefore(expireDate);
  }
}
