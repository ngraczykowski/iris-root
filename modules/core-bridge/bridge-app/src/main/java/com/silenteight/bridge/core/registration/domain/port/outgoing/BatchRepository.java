package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchId;

import java.util.Optional;

public interface BatchRepository {

  Optional<Batch> findById(String id);

  Optional<BatchId> findBatchIdByAnalysisName(String analysisName);

  Batch create(Batch batch);

  void updateStatusToCompleted(String batchId);

  void updateStatusAndErrorDescription(String batchId, BatchStatus status, String errorDescription);

  default void updateStatus(String batchId, BatchStatus status) {
    updateStatusAndErrorDescription(batchId, status, null);
  }
}
