package com.silenteight.bridge.core.reports.domain.model;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record Report(
    String batchId,
    String analysisName,
    AlertData alertData,
    List<MatchData> matches
) {

  @Builder
  public Report {}

  public static record AlertData(
      String id,
      String name,
      String recommendation,
      String comment,
      OffsetDateTime recommendedAt,
      String errorDescription,
      String status,
      String policyTitle,
      String policyId,
      Map<String, Object> metadata
  ) {

    @Builder(toBuilder = true)
    public AlertData {}
  }

  public static record MatchData(
      String id,
      String name,
      String recommendation,
      String comment,
      String stepTitle,
      String stepId,
      String fvSignature,
      Map<String, String> features,
      Map<String, String> categories
  ) {

    @Builder
    public MatchData {}
  }
}
