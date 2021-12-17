package com.silenteight.bridge.core.registration.domain;

import lombok.Builder;

public record Batch(String id, String analysisName, Long alertsCount, BatchStatus status) {

  @Builder
  public Batch {}

  public static Batch newOne(String id, String analysisName, Long alertsCount) {
    return new Batch(id, analysisName, alertsCount, BatchStatus.STORED);
  }

  public enum BatchStatus {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
