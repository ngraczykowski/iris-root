package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.BatchEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;
import com.silenteight.bridge.core.registration.domain.model.BatchPriorityWithStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JdbcBatchRepository implements BatchRepository {

  private final CrudBatchRepository crudBatchRepository;

  @Override
  public Optional<Batch> findById(String id) {
    return crudBatchRepository.findByBatchId(id).map(this::mapToBatch);
  }

  @Override
  public Optional<Batch> findBatchByAnalysisName(String analysisName) {
    return crudBatchRepository.findByAnalysisName(analysisName)
        .map(this::mapToBatch);
  }

  @Override
  public Optional<BatchIdWithPolicy> findBatchIdWithPolicyByAnalysisName(String analysisName) {
    return crudBatchRepository.findBatchIdWithPolicyByAnalysisName(analysisName)
        .map(projection -> new BatchIdWithPolicy(projection.batchId(), projection.policyName()));
  }

  @Override
  public Optional<BatchPriorityWithStatus> findBatchPriorityById(String id) {
    return crudBatchRepository.findPriorityByBatchId(id)
        .map(projection -> new BatchPriorityWithStatus(projection.priority(),
            BatchStatus.valueOf(projection.status().name())));
  }

  @Override
  public void updateStatusToCompleted(String batchId) {
    crudBatchRepository.updateStatusByBatchId(Status.COMPLETED.name(), batchId);
  }

  @Override
  public void updateStatusAndErrorDescription(
      String batchId, BatchStatus status, String errorDescription) {
    crudBatchRepository.updateStatusAndErrorDescription(
        batchId, Status.valueOf(status.name()), errorDescription);
  }

  @Override
  public void updateStatusToDelivered(String batchId) {
    crudBatchRepository.updateStatusByBatchId(Status.DELIVERED.name(), batchId);
  }

  @Override
  public Batch create(Batch batch) {
    var batchEntity = BatchEntity.builder()
        .batchId(batch.id())
        .analysisName(batch.analysisName())
        .policyName(batch.policyName())
        .alertsCount(batch.alertsCount())
        .priority(batch.batchPriority())
        .status(Status.valueOf(batch.status().name()))
        .errorDescription(batch.errorDescription())
        .isSimulation(batch.isSimulation())
        .batchMetadata(batch.batchMetadata())
        .build();
    crudBatchRepository.save(batchEntity);
    return batch;
  }

  private Batch mapToBatch(BatchEntity batchEntity) {
    return Batch.builder()
        .id(batchEntity.batchId())
        .analysisName(batchEntity.analysisName())
        .policyName(batchEntity.policyName())
        .alertsCount(batchEntity.alertsCount())
        .batchPriority(batchEntity.priority())
        .status(BatchStatus.valueOf(batchEntity.status().name()))
        .errorDescription(batchEntity.errorDescription())
        .isSimulation(batchEntity.isSimulation())
        .batchMetadata(batchEntity.batchMetadata())
        .build();
  }
}
