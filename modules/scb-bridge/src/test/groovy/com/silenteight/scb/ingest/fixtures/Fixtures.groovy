package com.silenteight.scb.ingest.fixtures

import com.silenteight.scb.ingest.domain.model.Batch
import com.silenteight.scb.ingest.domain.model.BatchMetadata

class Fixtures {

  static BatchMetadata BATCH_METADATA = new BatchMetadata()

  static def BATCH = Batch.builder()
      .id('batchId')
      .alertCount(3L)
      .metadata(BATCH_METADATA)
      .build()
}
