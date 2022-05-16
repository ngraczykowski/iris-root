package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

public record AlertsToRegister(
    List<AlertWithMatches> registerAlertsWithMatches
) {

  public static record AlertWithMatches(
      String alertId,
      Integer priority,
      List<Match> matches,
      OffsetDateTime alertTime) {

    @Builder
    public AlertWithMatches {}
  }

  public record Match(
      String matchId
  ) {}
}
