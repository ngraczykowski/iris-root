package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
class StreamRecommendationsUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationRepository repository;

  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> consumer) {

  }
}
