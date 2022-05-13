package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.Builder;

public record AlertWithoutMatches(
    String id,
    String alertId,
    String alertName,
    AlertStatus alertStatus,
    String metadata,
    String errorDescription) {

  @Builder
  public AlertWithoutMatches {}

  public enum AlertStatus {
    REGISTERED, PROCESSING, RECOMMENDED, ERROR, DELIVERED
  }
}
