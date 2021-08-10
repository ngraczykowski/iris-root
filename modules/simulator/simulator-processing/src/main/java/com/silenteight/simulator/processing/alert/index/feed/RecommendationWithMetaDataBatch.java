package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

import java.util.ArrayList;
import java.util.List;

class RecommendationWithMetaDataBatch {

  private final int batchSize;

  List<RecommendationWithMetadata> recommendations = new ArrayList<>();

  public RecommendationWithMetaDataBatch(int batchSize) {
    this.batchSize = batchSize;
  }

  public void addItem(RecommendationWithMetadata recommendation) {
    recommendations.add(recommendation);
  }

  public boolean isComplete() {
    return recommendations.size() >= batchSize;
  }

  public void clear() {
    recommendations.clear();
  }

  public List<RecommendationWithMetadata> getRecommendations() {
    return recommendations;
  }
}
