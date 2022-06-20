/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert;

import java.time.OffsetDateTime;

public interface RawAlertRepositoryExt {

  void createPartition(OffsetDateTime dateTime);

  void removeExpiredPartitions(OffsetDateTime expiredDate);
}
