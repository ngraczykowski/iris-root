package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record Batch(
    String id,
    Long alertCount,
    Priority priority,
    BatchSource source,
    BatchMetadata metadata) {

  @Builder
  public Batch {}

  public static Batch of(String batchId, Long alertCount, RegistrationBatchContext context) {
    return Batch.builder()
        .id(batchId)
        .alertCount(alertCount)
        .priority(context.priority())
        .source(context.batchSource())
        .metadata(new BatchMetadata(context.batchSource()))
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
