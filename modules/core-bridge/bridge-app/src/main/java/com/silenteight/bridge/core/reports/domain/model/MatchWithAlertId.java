package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

public record MatchWithAlertId(
    String alertId,
    String id,
    String name
) {

  @Builder
  public MatchWithAlertId {
  }
}
