package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.BatchEntity.Status;
import com.silenteight.bridge.core.registration.domain.Batch;
import com.silenteight.bridge.core.registration.domain.Batch.BatchStatus;
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
  public void updateStatus(String batchId, BatchStatus status) {
    crudBatchRepository.updateStatus(batchId, Status.valueOf(status.name()));
  }

  @Override
  public Batch create(Batch batch) {
    var batchEntity = BatchEntity.builder()
        .batchId(batch.id())
        .analysisName(batch.analysisName())
        .alertsCount(batch.alertsCount())
        .status(Status.valueOf(batch.status().name()))
        .build();
    crudBatchRepository.save(batchEntity);
    return batch;
  }

  private Batch mapToBatch(BatchEntity batchEntity) {
    return Batch.builder()
        .alertsCount(batchEntity.alertsCount())
        .id(batchEntity.batchId())
        .analysisName(batchEntity.analysisName())
        .status(BatchStatus.valueOf(batchEntity.status().name()))
        .build();
  }
}
