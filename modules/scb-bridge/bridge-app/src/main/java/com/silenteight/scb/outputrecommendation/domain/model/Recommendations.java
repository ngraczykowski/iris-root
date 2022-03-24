package com.silenteight.scb.outputrecommendation.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record Recommendations(
    List<Recommendation> recommendations,
    BatchStatistics statistics
) {

  @Builder
  public Recommendations {}

  public static record Recommendation(
      Alert alert,
      List<Match> matches,
      String batchId,
      String name,
      String recommendedAction,
      String recommendedComment,
      String policyId,
      OffsetDateTime recommendedAt
  ) {

    @Builder
    public Recommendation {}
  }

  public static record Alert(
      String id,
      AlertStatus status,
      String metadata,
      String errorMessage
  ) {

    @Builder
    public Alert {}
  }

  public static record Match(
      String id,
      String recommendedAction,
      String recommendedComment,
      String stepId,
      String fvSignature,
      Map<String, String> features
  ) {

    @Builder
    public Match {}
  }

  public enum AlertStatus {
    UNKNOWN, FAILURE, SUCCESS
  }
}
