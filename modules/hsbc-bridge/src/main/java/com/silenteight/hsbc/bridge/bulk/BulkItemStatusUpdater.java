package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkItemStatusEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
class BulkItemStatusUpdater {

  private final BulkItemRepository bulkItemRepository;

  @EventListener
  @Async
  @Transactional
  public void onUpdateBulkStatusEvent(UpdateBulkItemStatusEvent event) {
    var findResult = bulkItemRepository.findById(event.getBulkItemId());

    if (findResult.isPresent()) {
      updateBulkItemStatus(findResult.get(), event.getNewStatus());
    } else {
      log.error("BulkItem has not been found, id={}", event.getBulkItemId());
    }
  }

  private void updateBulkItemStatus(BulkItem bulkItem, BulkStatus bulkStatus) {
    bulkItem.setStatus(bulkStatus);
    bulkItemRepository.save(bulkItem);
  }
}
