package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;

import java.util.Optional;

public interface BatchRepository {

  Optional<Batch> findById(String id);

  Batch create(Batch batch);

  void updateStatusAndErrorDescription(String batchId, BatchStatus status, String errorDescription);

  default void updateStatus(String batchId, BatchStatus status) {
    updateStatusAndErrorDescription(batchId, status, null);
  }
}
