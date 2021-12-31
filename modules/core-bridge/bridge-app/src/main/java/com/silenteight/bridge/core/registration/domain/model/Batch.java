package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record Batch(String id,
                    String analysisName,
                    Long alertsCount,
                    BatchStatus status,
                    String errorDescription) {

  @Builder
  public Batch {}

  public static Batch newOne(String id, String analysisName, Long alertsCount) {
    return new Batch(id, analysisName, alertsCount, BatchStatus.STORED, "");
  }

  public static Batch error(String id, String errorDescription) {
    return new Batch(id, "", 0L, BatchStatus.ERROR, errorDescription);
  }

  public enum BatchStatus {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
