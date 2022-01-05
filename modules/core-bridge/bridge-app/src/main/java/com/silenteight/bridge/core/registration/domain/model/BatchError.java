package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record BatchError(String id, String errorDescription, String batchMetadata) {

  @Builder
  public BatchError {}
}
