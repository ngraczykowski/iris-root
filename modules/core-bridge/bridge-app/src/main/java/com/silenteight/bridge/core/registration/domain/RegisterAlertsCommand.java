package com.silenteight.bridge.core.registration.domain;

import lombok.Builder;

import java.util.List;

public record RegisterAlertsCommand(String batchId,
                                    List<AlertWithMatches> alertWithMatches) {

  public static record AlertWithMatches(String alertId,
                                        String errorDescription,
                                        List<Match> matches,
                                        AlertStatus alertStatus) {

    @Builder
    public AlertWithMatches {}
  }

  public static record Match(String id) {}

  public enum AlertStatus {
    SUCCESS, FAILURE
  }
}
