package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.BatchEntity.Status;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface CrudBatchRepository extends CrudRepository<BatchEntity, Long> {

  Optional<BatchEntity> findByBatchId(String batchId);

  Optional<BatchIdProjection> findByAnalysisName(String analysisName);

  @Query("""
      SELECT batch_id, policy_name
      FROM core_bridge_batches
      WHERE analysis_name = :analysisName""")
  Optional<BatchIdWithPolicyProjection> findBatchIdWithPolicyByAnalysisName(String analysisName);

  @Modifying
  @Query("""
      UPDATE core_bridge_batches 
      SET status = :status, error_description = :errorDescription, updated_at = NOW() 
      WHERE batch_id = :batchId""")
  void updateStatusAndErrorDescription(String batchId, Status status, String errorDescription);

  @Modifying
  @Query("""
      UPDATE core_bridge_batches 
      SET status = :status, updated_at = NOW() 
      WHERE batch_id = :batchId""")
  void updateStatusByBatchId(String status, String batchId);
}
