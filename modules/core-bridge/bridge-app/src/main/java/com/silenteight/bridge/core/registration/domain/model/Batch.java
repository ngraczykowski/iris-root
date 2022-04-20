package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record Batch(String id,
                    String analysisName,
                    String policyName,
                    Long alertsCount,
                    BatchStatus status,
                    String errorDescription,
                    String batchMetadata,
                    Integer batchPriority,
                    boolean isSimulation
) {

  private static final Integer BATCH_ERROR_PRIORITY = 0;
  private static final Long BATCH_ERROR_ALERTS_COUNT = 0L;
  private static final String BATCH_ERROR_POLICY_NAME = "";
  private static final String BATCH_ERROR_ANALYSIS_NAME = "";

  @Builder
  public Batch {}

  public static Batch error(
      String id, String errorDescription, String batchMetadata, boolean isSimulation) {
    return new Batch(
        id,
        BATCH_ERROR_ANALYSIS_NAME,
        BATCH_ERROR_POLICY_NAME,
        BATCH_ERROR_ALERTS_COUNT,
        BatchStatus.ERROR,
        errorDescription,
        batchMetadata,
        BATCH_ERROR_PRIORITY,
        isSimulation);
  }

  public enum BatchStatus {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
