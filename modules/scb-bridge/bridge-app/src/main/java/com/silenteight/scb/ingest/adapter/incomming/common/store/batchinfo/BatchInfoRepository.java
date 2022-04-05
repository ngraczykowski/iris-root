package com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo;

import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncConstants;
import com.silenteight.scb.ingest.domain.model.BatchStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BatchInfoRepository extends CrudRepository<BatchInfo, Long> {

  @Modifying
  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  @Query("UPDATE BatchInfo SET batchStatus = :status "
      + "WHERE internalBatchId = :internalBatchId")
  void update(@Param("internalBatchId") String internalBatchId,
      @Param("status") BatchStatus status);

}
