package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import java.util.List;

public record AddAlertToAnalysisCommand(
    String batchId,
    String alertId,
    String errorDescription,
    FeedingStatus feedingStatus,
    List<FedMatch> fedMatches
) {

  @Builder
  public AddAlertToAnalysisCommand {}

  public enum FeedingStatus {
    SUCCESS, FAILURE
  }

  public record FedMatch(
      String id
  ) {}
}
