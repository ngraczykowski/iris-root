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
      RecommendedAction recommendedAction,
      String recommendedComment,
      String policyId,
      OffsetDateTime recommendedAt
  ) {

    @Builder(toBuilder = true)
    public Recommendation {}
  }

  public static record Alert(
      String id,
      String name,
      AlertStatus status,
      String metadata,
      String errorMessage
  ) {

    @Builder
    public Alert {}
  }

  public static record Match(
      String id,
      String name,
      String recommendedAction,
      String recommendedComment,
      String stepId,
      String fvSignature,
      Map<String, String> features
  ) {

    @Builder(toBuilder = true)
    public Match {}
  }

  public enum AlertStatus {
    UNKNOWN, FAILURE, SUCCESS
  }

  public enum RecommendedAction {
    ACTION_INVESTIGATE,
    ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE,
    ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE,
    ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE,
    ACTION_FALSE_POSITIVE,
    ACTION_POTENTIAL_TRUE_POSITIVE
  }

}
