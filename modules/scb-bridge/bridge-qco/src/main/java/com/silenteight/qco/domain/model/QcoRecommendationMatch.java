package com.silenteight.qco.domain.model;

import lombok.Builder;

public record QcoRecommendationMatch(
    String batchId,
    String alertId,
    String alertName,
    String policyId,
    String matchName,
    String stepId,
    String comment,
    String solution) {

  @Builder
  public QcoRecommendationMatch {}
}