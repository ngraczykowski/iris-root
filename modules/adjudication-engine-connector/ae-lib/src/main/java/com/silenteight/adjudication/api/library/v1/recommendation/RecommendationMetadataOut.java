package com.silenteight.adjudication.api.library.v1.recommendation;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationMetadataOut {

  private List<MatchMetadataOut> matchesMetadata;
}
