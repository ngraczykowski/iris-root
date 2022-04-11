package com.silenteight.registration.api.library.v1;

class RegisterBatchInRequestFixtures {

  static final String BATCH_ID = "batch_id";
  static final String BATCH_METADATA = "batch_metadata";
  static final long ALERT_COUNT = 10;
  static final int BATCH_PRIORITY = 1;
  static final boolean BATCH_IS_SIMULATION = false;

  static final RegisterBatchIn REGISTER_BATCH_IN_WITH_NULL_PRIORITY =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .isSimulation(BATCH_IS_SIMULATION)
          .batchMetadata(BATCH_METADATA)
          .alertCount(ALERT_COUNT)
          .build();

  static final RegisterBatchIn REGISTER_BATCH_IN_WITH_NULL_SIMULATION =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .batchMetadata(BATCH_METADATA)
          .alertCount(ALERT_COUNT)
          .batchPriority(BATCH_PRIORITY)
          .build();

  static final RegisterBatchIn REGISTER_BATCH_IN_WITH_NULL_SIMULATION_AND_NULL_PRIORITY =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .batchMetadata(BATCH_METADATA)
          .alertCount(ALERT_COUNT)
          .build();

  static final RegisterBatchIn REGISTER_BATCH_IN =
      RegisterBatchIn.builder()
          .batchId(BATCH_ID)
          .isSimulation(BATCH_IS_SIMULATION)
          .batchMetadata(BATCH_METADATA)
          .alertCount(ALERT_COUNT)
          .batchPriority(BATCH_PRIORITY)
          .build();
}
