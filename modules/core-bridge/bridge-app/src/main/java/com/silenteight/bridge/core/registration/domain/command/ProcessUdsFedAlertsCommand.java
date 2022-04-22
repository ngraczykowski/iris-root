package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import java.util.List;

public record ProcessUdsFedAlertsCommand(
    String batchId,
    String alertName,
    String errorDescription,
    FeedingStatus feedingStatus,
    List<FedMatch> fedMatches
) {

  @Builder
  public ProcessUdsFedAlertsCommand {}

  public enum FeedingStatus {
    SUCCESS, FAILURE
  }

  public record FedMatch(
      String matchName
  ) {}
}
