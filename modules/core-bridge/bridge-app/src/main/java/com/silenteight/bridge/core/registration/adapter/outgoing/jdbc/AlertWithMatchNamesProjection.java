package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.Builder;

record AlertWithMatchNamesProjection(
    String alertId,
    String alertName,
    String alertStatus,
    String alertMetadata,
    String alertErrorDescription,
    String matchName,
    String matchId
) {

  @Builder
  public AlertWithMatchNamesProjection {}
}
