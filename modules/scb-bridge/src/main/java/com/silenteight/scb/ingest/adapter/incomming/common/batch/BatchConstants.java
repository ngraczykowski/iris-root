package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public final class BatchConstants {

  private static final String PREFIX = BatchConstants.class.getPackageName() + "#";

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class JobName {

    public static final String SCB_ALERT_LEVEL_SYNC = PREFIX + "scbAlertLevelSyncJob";
    public static final String SCB_WATCHLIST_LEVEL_SYNC = PREFIX + "scbWatchlistLevelSyncJob";
    public static final String SCB_ALERT_LEVEL_LEARNING_SYNC =
        PREFIX + "scbAlertLevelLearningSyncJob";
    public static final String SCB_WATCHLIST_LEVEL_LEARNING_SYNC =
        PREFIX + "scbWatchlistLevelLearningSyncJob";
    public static final String ECM_LEARNING_SYNC = PREFIX + "ecmLearningSyncJob";
  }

  private BatchConstants() {
  }
}
