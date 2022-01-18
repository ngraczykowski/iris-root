package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record BatchError(String id,
                         String batchMetadata,
                         String errorDescription,
                         BatchStatistics statistics) {

  @Builder
  public BatchError {}
}
