package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BatchCancelResponse;

import javax.transaction.Transactional;

@RequiredArgsConstructor
class CancelBulkUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BatchCancelResponse cancel(String id) {
    //check if in ERROR / INPROCESS
    bulkRepository.updateStatusById(id, BulkStatus.CANCELLED);

    BatchCancelResponse response = new BatchCancelResponse();
    response.batchId(id);
    response.batchStatus(BatchStatus.CANCELLED);
    return response;
  }
}
