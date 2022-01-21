package com.silenteight.bridge.core.registration.domain;

import lombok.Builder;

import java.util.List;

public record AddAlertToAnalysisCommand(
    String batchId,
    String alertId,
    FeedingStatus feedingStatus,
    List<FedMatch> fedMatches
) {

  @Builder
  public AddAlertToAnalysisCommand {}

  public enum FeedingStatus {
    SUCCESS, FAILURE
  }

  public record FedMatch(
      String id,
      FeedingStatus feedingStatus
  ) {}
}
