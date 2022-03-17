package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;

public record Batch(
    String id,
    Long alertCount,
    BatchMetadata metadata) {

  public Batch(String id, Long alertCount) {
    this(id, alertCount, null);
  }

  @Builder
  public Batch {}
}
