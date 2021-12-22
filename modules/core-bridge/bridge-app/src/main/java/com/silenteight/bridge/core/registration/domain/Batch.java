package com.silenteight.bridge.core.registration.domain;

import lombok.Builder;

public record Batch(String id, Long alertsCount, BatchStatus status, String analysisName) {

  @Builder
  public Batch {}

  public static Batch newOne(String id, String analysisName, Long alertsCount) {
    return new Batch(id, alertsCount, BatchStatus.STORED, analysisName);
  }

  public static Batch error(String id) {
    return new Batch(id, 0L, BatchStatus.ERROR, "");
  }

  public enum BatchStatus {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
