package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

public record RegisterAlertsCommand(
    String batchId,
    List<AlertWithMatches> alertWithMatches
) {

  public static record AlertWithMatches(
      String alertId,
      String errorDescription,
      String alertMetadata,
      List<Match> matches,
      AlertStatus alertStatus,
      OffsetDateTime alertTime
  ) {

    @Builder
    public AlertWithMatches {}
  }

  public record Match(
      String id
  ) {}

  public enum AlertStatus {
    SUCCESS, FAILURE
  }
}
