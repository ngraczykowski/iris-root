package com.silenteight.simulator.processing.alert.index.feed;

import lombok.NonNull;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;

public interface RecommendationMetadataService {

  RecommendationMetadata getMetadata(@NonNull String recommendationName);
}
