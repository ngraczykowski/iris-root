package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.output.BulkCancelResponse;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class CancelBulkUseCase {

  private final BulkRepository bulkRepository;

  @Transactional
  public BulkCancelResponse cancel(String id) {
    //check if in ERROR / INPROCESS
    bulkRepository.updateStatusById(id, BulkStatus.CANCELLED);

    BulkCancelResponse response = new BulkCancelResponse();
    response.bulkId(id);
    response.bulkStatus(com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus.CANCELLED);
    return response;
  }
}
