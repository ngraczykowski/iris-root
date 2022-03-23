package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.AlertUnderProcessingKey;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;

interface AlertUnderProcessingRepository
    extends Repository<AlertUnderProcessing, AlertUnderProcessingKey> {

  Collection<AlertUnderProcessing> findAll();

  Collection<AlertUnderProcessing> findAllBySystemIdIn(Collection<String> systemIds);

  void saveAll(Iterable<AlertUnderProcessing> entities);

  void deleteBySystemIdAndBatchId(String systemId, String batchId);

  void deleteByCreatedAtBefore(OffsetDateTime expireDate);

  @Modifying
  @Query("UPDATE AlertUnderProcessing SET state = :state"
      + " WHERE systemId = :systemId AND batchId = :batchId")
  void update(
      @Param("systemId") String systemId,
      @Param("batchId") String batchId,
      @Param("state") State state
  );

  @Modifying
  @Query("UPDATE AlertUnderProcessing SET state = :state, error = :error"
      + " WHERE systemId = :systemId AND batchId = :batchId")
  void update(
      @Param("systemId") String systemId,
      @Param("batchId") String batchId,
      @Param("state") State state,
      @Param("error") String error
  );

  Collection<AlertUnderProcessing> findAllByInternalBatchId(String internalBatchId);
}
