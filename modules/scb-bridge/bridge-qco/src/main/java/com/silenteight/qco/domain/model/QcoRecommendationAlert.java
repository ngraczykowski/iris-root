package com.silenteight.qco.domain.model;

import lombok.Builder;

import java.util.List;

public record QcoRecommendationAlert(
    String batchId,
    String alertId,
    String alertName,
    String policyId,
    List<QcoMatchData> matches) {

  @Builder(toBuilder = true)
  public QcoRecommendationAlert {}

  public static record QcoMatchData(
      String name,
      String recommendation,
      String comment,
      String stepId
  ) {

    @Builder(toBuilder = true)
    public QcoMatchData {}
  }
}
