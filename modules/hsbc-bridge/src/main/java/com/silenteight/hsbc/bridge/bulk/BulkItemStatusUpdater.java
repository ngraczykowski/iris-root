package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class BulkItemStatusUpdater {

  private final BulkItemRepository bulkItemRepository;
  private final BulkStatusUpdater bulkStatusUpdater;

  @Transactional
  public void update(long bulkItemId, @NonNull BulkStatus status) {
    bulkItemRepository.findById(bulkItemId).ifPresent(bulkItem -> {
      bulkItem.setStatus(status);
      bulkItemRepository.save(bulkItem);

      bulkStatusUpdater.recalculateBulkStatus(bulkItem.getBulkId());
    });
  }
}
