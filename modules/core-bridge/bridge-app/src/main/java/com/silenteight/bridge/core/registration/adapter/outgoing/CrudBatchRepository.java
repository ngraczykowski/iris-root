package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.BatchEntity.Status;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface CrudBatchRepository extends CrudRepository<BatchEntity, Long> {

  Optional<BatchEntity> findByBatchId(String batchId);

  @Modifying
  @Query("UPDATE batches "
      + "SET status = :status, error_description = :errorDescription WHERE batch_id = :batchId")
  void updateStatusAndErrorDescription(String batchId, Status status, String errorDescription);
}
