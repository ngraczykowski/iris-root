/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.Builder;

public record PrepareErrorRecommendationResponseCommand(
    String batchId,
    String errorDescription,
    String batchMetadata
) {

  @Builder
  public PrepareErrorRecommendationResponseCommand {}
}
