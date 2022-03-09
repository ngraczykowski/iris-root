package com.silenteight.scb.ingest.domain.model;

import lombok.Builder;

public record Batch(
    String id,
    Long alertCount,
    BatchMetadata metadata
) {

  @Builder
  public Batch {}
}
