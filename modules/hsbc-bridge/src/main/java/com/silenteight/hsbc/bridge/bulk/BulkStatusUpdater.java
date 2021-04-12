package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkStatusEvent;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.ERROR;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
class BulkStatusUpdater {

  private final BulkRepository bulkRepository;

  @EventListener
  @Async
  public void onBulkStatusEvent(UpdateBulkStatusEvent event) {
    var bulk = bulkRepository.findById(event.getBulkId());
    determineNewBulkStatus(bulk.getItems())
        .ifPresent(status -> updateBulkStatus(bulk, status));
  }

  private Optional<BulkStatus> determineNewBulkStatus(Collection<BulkItem> items) {
    if (atLeastOneProcessing(items)) {
      return of(PROCESSING);
    } else if (notEmptyAndAllCompletedOrClosed(items)) {
      return of(COMPLETED);
    } else {
      return empty();
    }
  }

  private boolean atLeastOneProcessing(Collection<BulkItem> items) {
    return items.stream().map(BulkItem::getStatus)
        .anyMatch(status -> status == PROCESSING);
  }

  private boolean notEmptyAndAllCompletedOrClosed(Collection<BulkItem> items) {
    if (items.isEmpty()) {
      return false;
    } else {
      return items.stream().map(BulkItem::getStatus)
          .allMatch(status -> status == COMPLETED || status == ERROR);
    }
  }

  @Transactional
  void updateBulkStatus(Bulk bulk, BulkStatus bulkStatus) {
    bulk.setStatus(bulkStatus);
    bulkRepository.save(bulk);
  }
}
