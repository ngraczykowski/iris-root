package com.silenteight.fab.dataprep.domain;

import java.time.OffsetDateTime;

interface AlertRepositoryExt {

  void createPartition(OffsetDateTime rangeFrom);
}
