package com.silenteight.registration.api.library.v1;

import com.silenteight.registration.api.library.v1.RegisterBatchIn;

class RegisterBatchInRequestFixtures {

  static final String BATCH_ID = "batch_id";
  static final long ALERT_COUNT = 10;

  static final RegisterBatchIn REGISTER_BATCH_IN_REQUEST = RegisterBatchIn.builder()
      .batchId(BATCH_ID)
      .alertCount(ALERT_COUNT)
      .build();
}
