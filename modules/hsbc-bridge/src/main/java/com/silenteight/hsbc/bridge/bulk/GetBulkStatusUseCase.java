package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatusResponse;

import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PRE_PROCESSED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;
import static java.util.List.of;

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
    return bulkRepository.existsByStatusIn(of(STORED, PRE_PROCESSED, PROCESSING));
  }
}
