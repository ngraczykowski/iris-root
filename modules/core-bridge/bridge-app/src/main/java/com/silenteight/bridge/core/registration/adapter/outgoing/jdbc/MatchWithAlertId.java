package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

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
