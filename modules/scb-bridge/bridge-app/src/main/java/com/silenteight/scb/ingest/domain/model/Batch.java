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

  public static Batch of(String batchId, Long alertCount, RegistrationAlertContext context) {
    return Batch.builder()
        .id(batchId)
        .alertCount(alertCount)
        .priority(context.priority())
        .metadata(new BatchMetadata(context.alertSource()))
        .build();
  }

  @Getter
  @RequiredArgsConstructor
  public enum Priority {
    LOW(1),
    MEDIUM(5),
    HIGH(10);

    private final int value;
  }
}
