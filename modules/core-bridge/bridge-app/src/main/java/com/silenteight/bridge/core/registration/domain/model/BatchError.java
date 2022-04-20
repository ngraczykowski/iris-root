package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record BatchError(String id,
                         String batchMetadata,
                         String errorDescription,
                         boolean isSimulation
) {

  @Builder
  public BatchError {}
}
