package com.silenteight.adjudication.api.library.v1.recommendation;

import java.util.Collection;

public interface RecommendationServiceClient {

  Collection<RecommendationWithMetadataOut> getRecommendations(String analysis);
}
