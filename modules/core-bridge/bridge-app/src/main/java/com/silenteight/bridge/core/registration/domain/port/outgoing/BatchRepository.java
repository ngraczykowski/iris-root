package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.Batch;
import com.silenteight.bridge.core.registration.domain.Batch.BatchStatus;

import java.util.Optional;

public interface BatchRepository {

  Optional<Batch> findById(String id);

  Batch create(Batch batch);

  void updateStatus(String batchId, BatchStatus status);
}
