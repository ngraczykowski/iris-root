package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import java.time.OffsetDateTime;

public interface RawAlertRepositoryExt {

  void createPartition(OffsetDateTime dateTime);

  void removeExpiredPartitions(OffsetDateTime expiredDate);
}
