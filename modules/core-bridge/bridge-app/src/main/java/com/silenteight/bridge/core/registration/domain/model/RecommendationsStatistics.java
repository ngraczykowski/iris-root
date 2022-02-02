package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record RecommendationsStatistics(Integer truePositiveCount,
                                        Integer falsePositiveCount,
                                        Integer manualInvestigationCount) {

  @Builder
  public RecommendationsStatistics {
  }
}
