package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

  @Transactional
  public void recalculateBulkStatus(@NonNull String bulkId) {
    var bulk = bulkRepository.findById(bulkId);

    if (bulk.hasNonFinalStatus()) {
      determineNewBulkStatus(bulk.getItems())
          .ifPresent(status -> updateBulkStatus(bulk, status));
    }
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

  private void updateBulkStatus(Bulk bulk, BulkStatus bulkStatus) {
    bulk.setStatus(bulkStatus);
    bulkRepository.save(bulk);
  }
}
