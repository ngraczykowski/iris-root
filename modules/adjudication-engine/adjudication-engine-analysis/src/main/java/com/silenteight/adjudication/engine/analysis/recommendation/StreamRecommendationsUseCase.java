package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
class StreamRecommendationsUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationRepository repository;

  @Transactional(readOnly = true)
  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> consumer) {

    if (log.isDebugEnabled()) {
      log.debug("Streaming recommendations: request={}", request);
    }

    var analysisId = ResourceName.create(request.getAnalysis()).getLong("analysis");

    repository.findAllByAnalysisId(analysisId)
        .map(RecommendationEntity::toRecommendation)
        .forEach(consumer);
  }
}
