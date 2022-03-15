package com.silenteight.bridge.core.registration.domain.model;

import java.util.List;

public record AlertsToRegister(
    List<AlertWithMatches> registerAlertsWithMatches
) {

  public record AlertWithMatches(
      String alertId,
      Integer priority,
      List<Match> matches
  ) {}

  public record Match(
      String matchId
  ) {}
}
