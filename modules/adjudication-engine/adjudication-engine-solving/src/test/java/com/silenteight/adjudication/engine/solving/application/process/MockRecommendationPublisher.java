/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;

import java.util.ArrayList;
import java.util.List;

class MockRecommendationPublisher implements RecommendationPublisher {

  private final List<RecommendationsGenerated> recommendationsGeneratedList = new ArrayList<>();

  @Override
  public void publish(RecommendationsGenerated recommendationsGenerated) {
    recommendationsGeneratedList.add(recommendationsGenerated);
  }

  public int getRecommendationCount() {
    return recommendationsGeneratedList
        .stream()
        .flatMap(r -> r.getRecommendationInfosList().stream())
        .toList()
        .size();
  }
}
