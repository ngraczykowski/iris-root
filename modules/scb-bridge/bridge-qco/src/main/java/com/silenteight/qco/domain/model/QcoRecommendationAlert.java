package com.silenteight.qco.domain.model;

import lombok.Builder;

import java.util.List;

public record QcoRecommendationAlert(
    String batchId,
    String alertId,
    String alertName,
    String policyId,
    List<QcoMatchData> matches,
    boolean onlyMark) {

  @Builder(toBuilder = true)
  public QcoRecommendationAlert {}

  public static record QcoMatchData(
      String id,
      String name,
      String recommendation,
      String comment,
      String stepId,
      String fvSignature,
      boolean qcoMarked
  ) {

    @Builder(toBuilder = true)
    public QcoMatchData {}
  }
}
