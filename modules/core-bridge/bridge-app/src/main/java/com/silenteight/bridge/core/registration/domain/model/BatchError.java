package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record BatchError(String id, String errorDescription) {

  @Builder
  public BatchError {}
}
