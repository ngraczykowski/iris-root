package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;

import java.util.List;

public record BatchWithAlertsDto(
    String batchId,
    String policyId,
    List<AlertWithMatchesDto> alerts
) {

  @Builder
  public BatchWithAlertsDto {}

  public static record AlertWithMatchesDto(
      String id,
      String name,
      AlertStatus status,
      String metadata,
      String errorDescription,
      List<MatchDto> matches
  ) {

    @Builder
    public AlertWithMatchesDto {}

    public static record MatchDto(
        String id,
        String name
    ) {}
  }

  public enum AlertStatus {
    REGISTERED, PROCESSING, RECOMMENDED, ERROR, DELIVERED
  }
}
