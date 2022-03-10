package com.silenteight.connector.ftcc.ingest.adapter.outgoing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.Batch;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RegistrationFixtures {

  private static final String BATCH_ID = "558ecea2-a1d5-11eb-bcbc-0242ac130002";
  static final Batch BATCH = Batch.builder()
      .batchId(BATCH_ID)
      .alertsCount(2L)
      .build();
}
