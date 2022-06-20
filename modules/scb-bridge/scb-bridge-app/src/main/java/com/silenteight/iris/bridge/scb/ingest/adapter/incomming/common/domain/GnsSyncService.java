/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class GnsSyncService {

  private final GnsSyncRepository syncRepository;

  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void finishSync(long syncId) {
    log.info("Finish gnsparty sync: {}", syncId);
    GnsSync gnsSync = syncRepository.getById(syncId);
    gnsSync.finishSync();
    syncRepository.save(gnsSync);
  }

  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void finishSyncWithError(long syncId, String errorMessage) {
    GnsSync gnsSync = syncRepository.getById(syncId);
    gnsSync.finishSyncWithError(errorMessage);
    syncRepository.save(gnsSync);
  }

  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public void abandonSync(@NonNull String syncMode) {
    List<GnsSync> runningSyncs = syncRepository.findAllBySyncModeAndFinishedAtIsNull(syncMode);

    if (!runningSyncs.isEmpty()) {
      for (GnsSync sync : runningSyncs) {
        sync.finishSyncWithError("ABANDONED");
        syncRepository.save(sync);
        log.info("Sync {} has been abandoned.", sync.getId());
      }
    } else {
      log.info("No running syncs found.");
    }
  }

  public boolean isRunningSync(@NonNull String syncMode) {
    List<GnsSync> activeSyncs = syncRepository.findAllBySyncModeAndFinishedAtIsNull(syncMode);
    return !activeSyncs.isEmpty();
  }

  @Transactional(GnsSyncConstants.PRIMARY_TRANSACTION_MANAGER)
  public Long startNewSync(@NonNull String syncMode) {
    GnsSync sync = new GnsSync();
    sync.setSyncMode(syncMode);
    syncRepository.save(sync);
    return sync.getId();
  }
}
