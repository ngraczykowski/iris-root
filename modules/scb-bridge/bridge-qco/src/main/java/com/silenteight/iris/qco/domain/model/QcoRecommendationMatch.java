/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.model;

import lombok.Builder;

public record QcoRecommendationMatch(
    String batchId,
    String alertId,
    String alertName,
    String policyId,
    String matchName,
    String stepId,
    String comment,
    String solution,
    boolean onlyMark) {

  @Builder
  public QcoRecommendationMatch {}
}
