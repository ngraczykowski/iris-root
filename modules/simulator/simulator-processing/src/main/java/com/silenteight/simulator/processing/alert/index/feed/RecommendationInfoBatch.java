package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;

import java.util.ArrayList;
import java.util.List;

class RecommendationInfoBatch {

  private final int batchSize;

  private final List<RecommendationInfo> recommendations;

  public RecommendationInfoBatch(int batchSize) {
    this.batchSize = batchSize;
    this.recommendations = new ArrayList<>(batchSize);
  }

  public void addItem(RecommendationInfo recommendationInfo) {
    recommendations.add(recommendationInfo);
  }

  public boolean isComplete() {
    return recommendations.size() >= batchSize;
  }

  public void clear() {
    recommendations.clear();
  }

  public List<RecommendationInfo> getRecommendations() {
    return recommendations;
  }
}
