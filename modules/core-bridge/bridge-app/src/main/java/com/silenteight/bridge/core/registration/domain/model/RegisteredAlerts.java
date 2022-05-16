package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

public record RegisteredAlerts(
    List<AlertWithMatches> registeredAlertsWithMatches
) {

  public static record AlertWithMatches(
      String alertId,
      String name,
      String metadata,
      List<Match> matches,
      OffsetDateTime alertTime
  ) {

    @Builder
    public AlertWithMatches {
    }
  }

  public record Match(
      String matchId,
      String name
  ) {}
}
