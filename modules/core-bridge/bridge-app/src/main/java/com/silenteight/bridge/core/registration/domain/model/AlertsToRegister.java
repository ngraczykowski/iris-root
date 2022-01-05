package com.silenteight.bridge.core.registration.domain.model;

import java.util.List;

public record AlertsToRegister(List<AlertWithMatches> registerAlertsWithMatches) {

  public static record AlertWithMatches(String alertId,
                                        List<Match> matches) {}

  public static record Match(String matchId) {}
}
