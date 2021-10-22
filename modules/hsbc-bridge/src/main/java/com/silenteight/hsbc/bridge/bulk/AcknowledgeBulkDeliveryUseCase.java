package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchResultNotAvailableException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchResultNotAvailableException.Reason;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class AcknowledgeBulkDeliveryUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BatchStatusResponse apply(@NonNull String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    if (batch.isNotCompleted()) {
      throw new BatchResultNotAvailableException(id, Reason.NOT_COMPLETED);
    }

    batch.delivered();
    bulkRepository.save(batch);

    return BatchResponseCreator.create(batch);
  }
}
