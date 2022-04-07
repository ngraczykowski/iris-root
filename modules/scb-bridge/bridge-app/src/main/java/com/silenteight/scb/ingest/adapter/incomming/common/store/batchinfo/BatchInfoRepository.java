package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import com.silenteight.scb.ingest.domain.model.BatchStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BatchInfoRepository extends CrudRepository<BatchInfo, Long> {

  @Modifying
  @Query("UPDATE BatchInfo SET batchStatus = :status "
      + "WHERE internalBatchId = :internalBatchId")
  void update(@Param("internalBatchId") String internalBatchId,
      @Param("status") BatchStatus status);

}
