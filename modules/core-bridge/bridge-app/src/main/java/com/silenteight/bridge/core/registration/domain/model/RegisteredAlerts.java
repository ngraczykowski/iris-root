package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record RegisteredAlerts(List<AlertWithMatches> registeredAlertsWithMatches) {

  public static record AlertWithMatches(String alertId,
                                        String name,
                                        List<Match> matches) {

    @Builder
    public AlertWithMatches {
    }
  }

  public static record Match(String matchId,
                             String name) {}
}
