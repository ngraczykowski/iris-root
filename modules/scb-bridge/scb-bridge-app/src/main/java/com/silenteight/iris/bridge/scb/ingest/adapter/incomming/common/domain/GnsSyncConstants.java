/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GnsSyncConstants {

  public static final String GNS_SYNC_ID_KEY = "GNS_SYNC_ID";
  public static final String GNS_SYNC_MODE_KEY = "GNS_SYNC_MODE";
  public static final String PRIMARY_TRANSACTION_MANAGER = "transactionManager";
}
