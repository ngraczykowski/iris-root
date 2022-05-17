package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record AlertToRetention(
    long alertPrimaryId,
    String alertId,
    String alertName,
    String batchId) {

  @Builder
  public AlertToRetention {}
}
