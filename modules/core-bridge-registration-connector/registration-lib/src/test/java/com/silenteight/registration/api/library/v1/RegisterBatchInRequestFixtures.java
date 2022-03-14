package com.silenteight.registration.api.library.v1;

class RegisterBatchInRequestFixtures {

  static final String BATCH_ID = "batch_id";
  static final long ALERT_COUNT = 10;
  static final int BATCH_PRIORITY = 1;

  static final RegisterBatchIn REGISTER_BATCH_IN_WITH_NULL_PRIORITY =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .batchMetadata("batch_metadata")
          .alertCount(ALERT_COUNT)
          .batchPriority(null)
          .build();

  static final RegisterBatchIn REGISTER_BATCH_IN =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .batchMetadata("batch_metadata")
          .alertCount(ALERT_COUNT)
          .batchPriority(BATCH_PRIORITY)
          .build();
}
