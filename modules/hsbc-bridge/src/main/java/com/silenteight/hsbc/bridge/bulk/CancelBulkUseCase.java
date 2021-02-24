package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository;
import com.silenteight.hsbc.bridge.rest.model.output.BulkCancelResponse;

import java.util.UUID;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class CancelBulkUseCase {

  private final BulkWriteRepository writeRepository;

  @Transactional
  public BulkCancelResponse cancel(UUID id) {
    //check if in ERROR / INPROCESS
    writeRepository.updateStatusById(id, BulkStatus.CANCELLED);

    BulkCancelResponse response = new BulkCancelResponse();
    response.bulkId(id);
    response.bulkStatus(com.silenteight.hsbc.bridge.rest.model.output.BulkStatus.CANCELLED);
    return response;
  }
}
