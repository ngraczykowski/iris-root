package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record AlertToRetention(
    String id,
    String name,
    String batchId) {

  @Builder
  public AlertToRetention {}
}
