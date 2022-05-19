package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

public record AlertWithoutMatchesDto(
    String id,
    String alertId,
    String alertName,
    String alertStatus,
    String metadata,
    String errorDescription) {

  @Builder
  public AlertWithoutMatchesDto {}
}
