package com.silenteight.registration.api.library.v1;

class NotifyBatchErrorRequestFixtures {

  static final String BATCH_ID = "batch_id";
  static final String ERROR_CAUSE = "error_cause";
  static final String BATCH_METADATA = "batch_metadata";

  static final NotifyBatchErrorIn NOTIFY_BATCH_ERROR = NotifyBatchErrorIn.builder()
      .batchId(BATCH_ID)
      .errorDescription(ERROR_CAUSE)
      .batchMetadata(BATCH_METADATA)
      .build();
}
