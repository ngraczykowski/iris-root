package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.BatchEntity.Status;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface CrudBatchRepository extends CrudRepository<BatchEntity, Long> {

  Optional<BatchEntity> findByBatchId(String batchId);

  @Modifying
  @Query("UPDATE BATCHES SET status = :status WHERE batch_id = :batchId")
  void updateStatus(@Param("batchId") String batchId, @Param("status") Status status);
}
