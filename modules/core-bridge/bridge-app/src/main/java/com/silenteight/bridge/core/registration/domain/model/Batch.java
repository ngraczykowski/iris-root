package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record Batch(String id,
                    String analysisName,
                    String policyName,
                    Long alertsCount,
                    BatchStatus status,
                    String errorDescription,
                    String batchMetadata) {

  @Builder
  public Batch {}

  public static Batch error(String id, String errorDescription, String batchMetadata) {
    return new Batch(id, "", "", 0L, BatchStatus.ERROR, errorDescription, batchMetadata);
  }

  public enum BatchStatus {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
