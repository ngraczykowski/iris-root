package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
class GetBulkStatusUseCase {

  private final BulkRepository bulkRepository;

  @Transactional(readOnly = true)
  public BatchStatusResponse getStatus(String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    return BatchResponseCreator.create(batch);
  }

  public boolean isProcessing() {
    return bulkRepository.existsByStatusIn(List.of(BulkStatus.STORED, BulkStatus.PRE_PROCESSED, BulkStatus.PROCESSING));
  }
}
