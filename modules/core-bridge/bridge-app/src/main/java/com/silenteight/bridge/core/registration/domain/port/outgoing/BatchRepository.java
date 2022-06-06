package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;
import com.silenteight.bridge.core.registration.domain.model.BatchPriority;
import com.silenteight.bridge.core.registration.domain.model.BatchPriorityWithStatus;

import java.util.Optional;

public interface BatchRepository {

  Optional<Batch> findById(String id);

  Optional<Batch> findBatchByAnalysisName(String analysisName);

  Optional<BatchIdWithPolicy> findBatchIdWithPolicyByAnalysisName(String analysisName);

  Optional<BatchPriorityWithStatus> findBatchPriorityById(String id);

  BatchPriority findBatchPriorityByAnalysisName(String analysisName);

  Batch create(Batch batch);

  void updateStatusToCompleted(String batchId);

  void updateStatusAndErrorDescription(String batchId, BatchStatus status, String errorDescription);

  void updateStatusToDelivered(String batchId);

  default void updateStatus(String batchId, BatchStatus status) {
    updateStatusAndErrorDescription(batchId, status, null);
  }
}
