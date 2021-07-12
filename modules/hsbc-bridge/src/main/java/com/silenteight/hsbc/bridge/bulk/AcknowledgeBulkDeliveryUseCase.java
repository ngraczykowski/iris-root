package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AcknowledgeBulkDeliveryUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BatchStatusResponse apply(@NonNull String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    if (batch.isNotCompleted()) {
      throw new BatchProcessingNotCompletedException(id);
    }

    batch.delivered();
    bulkRepository.save(batch);

    return BatchResponseCreator.create(batch);
  }
}
