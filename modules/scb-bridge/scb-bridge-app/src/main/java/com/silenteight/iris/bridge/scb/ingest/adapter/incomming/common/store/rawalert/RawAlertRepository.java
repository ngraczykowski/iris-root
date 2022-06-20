/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.UUID;

public interface RawAlertRepository extends CrudRepository<RawAlert, UUID>, RawAlertRepositoryExt {

  @Modifying
  @Query("DELETE FROM RawAlert")
  void deleteAll();

  @Modifying
  @Query("""
      UPDATE RawAlert
      SET payload = null
      WHERE internalBatchId =:internalBatchId AND systemId in :systemIds""")
  void clearPayloadByInternalBatchIdAndSystemIdIn(String internalBatchId, Set<String> systemIds);
}
