package com.silenteight.bridge.core.recommendation.domain.command;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;

import java.util.List;

public record ProceedReadyRecommendationsCommand(
    List<RecommendationWithMetadata> recommendationsWithMetadata
) {}
