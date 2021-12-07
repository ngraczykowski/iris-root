package com.silenteight.registration.api.library.v1;

import com.silenteight.registration.api.library.v1.NotifyBatchErrorIn;

class NotifyBatchErrorRequestFixtures {

  static final String BATCH_ID = "batch_id";

  static final NotifyBatchErrorIn NOTIFY_BATCH_ERROR = NotifyBatchErrorIn.builder()
      .batchId(BATCH_ID)
      .build();
}
