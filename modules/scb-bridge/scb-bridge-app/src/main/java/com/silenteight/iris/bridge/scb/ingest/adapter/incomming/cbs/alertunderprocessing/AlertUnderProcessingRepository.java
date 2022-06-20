/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.AlertUnderProcessingKey;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;

interface AlertUnderProcessingRepository
    extends Repository<AlertUnderProcessing, AlertUnderProcessingKey> {

  Collection<AlertUnderProcessing> findAll();

  @Query(
      "SELECT new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId("
          + "aup.systemId, aup.batchId) "
          + "FROM AlertUnderProcessing aup "
          + "WHERE aup.systemId IN ?1")
  Collection<AlertId> findAllBySystemIdIn(Collection<String> systemIds);

  void saveAll(Iterable<AlertUnderProcessing> entities);

  void deleteBySystemIdAndBatchId(String systemId, String batchId);

  void deleteByCreatedAtBeforeAndStateIn(OffsetDateTime expireDate, EnumSet<State> states);

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

  @Query(value = "SELECT * FROM SCB_CBS_ALERT_UNDER_PROCESSING "
      + "WHERE state = :#{#state.name()} "
      + "ORDER BY priority DESC "
      + "LIMIT :limit", nativeQuery = true)
  Collection<AlertUnderProcessing> findTopNByStateOrderByPriorityDesc(
      @Param("state") AlertUnderProcessing.State state, int limit);

  long countByState(AlertUnderProcessing.State state);
}
