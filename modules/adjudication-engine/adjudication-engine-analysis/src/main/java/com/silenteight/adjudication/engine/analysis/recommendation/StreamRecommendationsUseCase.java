package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
class StreamRecommendationsUseCase {

  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final RecommendationDataAccess recommendationDataAccess;

  @Transactional(readOnly = true)
  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> consumer) {

    if (log.isDebugEnabled()) {
      log.debug("Streaming recommendations: request={}", request);
    }

    var resource = request.getDataset().isEmpty() ? request.getAnalysis() : request.getDataset();

    var recommendationCount = readAlertRecommendations(resource, ar -> {
      var comment = generateCommentsUseCase
          .generateComments(new GenerateCommentsRequest(ar.getAlertContext()))
          .getComment();

      var recommendation = ar.toRecommendation(comment);

      consumer.accept(recommendation);

    });

    log.info("Finished streaming recommendations: request={}, recommendationCount={}",
        request, recommendationCount);
  }

  @SuppressWarnings("FeatureEnvy")
  private int readAlertRecommendations(
      String analysisOrDataset, Consumer<AlertRecommendation> consumer) {

    var resource = ResourceName.create(analysisOrDataset);

    if (resource.contains("datasets")) {
      return recommendationDataAccess.streamAlertRecommendations(
          resource.getLong("analysis"), resource.getLong("datasets"), consumer);
    }

    return recommendationDataAccess.streamAlertRecommendations(
        resource.getLong("analysis"), consumer);
  }
}
