package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Batch(
    String id,
    Long alertCount,
    Priority priority,
    BatchMetadata metadata) {

  public Batch(String id, Long alertCount, Priority priority) {
    this(id, alertCount, priority, null);
  }

  @Builder
  public Batch {}

  @Getter
  @RequiredArgsConstructor
  public enum Priority {
    LOW(1),
    MEDIUM(5),
    HIGH(10);

    private final int value;
  }
}
