package com.silenteight.hsbc.bridge.recommendation.metadata;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationMetadata {

  private List<MatchMetadata> matchesMetadata;
}
