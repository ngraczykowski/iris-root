package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
@Slf4j
class StreamRecommendationsUseCase {

  private final RecommendationDataAccess recommendationDataAccess;

  @Transactional(readOnly = true)
  void streamRecommendations(String analysisOrDataset, Consumer<Recommendation> consumer) {
    log.debug("Streaming recommendations: resource={}", analysisOrDataset);

    var recommendationCount = readAlertRecommendations(
        analysisOrDataset, ar -> consumer.accept(ar.toRecommendation()));

    log.info("Finished streaming recommendations: resource={}, recommendationCount={}",
        analysisOrDataset, recommendationCount);
  }

  @Transactional(readOnly = true)
  void streamRecommendationsWithMetadata(
      String analysisOrDataset, Consumer<RecommendationWithMetadata> consumer) {

    log.debug("Streaming recommendations with metadata: resource={}", analysisOrDataset);

    var recommendationCount = readAlertRecommendations(
        analysisOrDataset, ar -> consumer.accept(generateRecommendationWithMetadata(ar)));

    log.info("Finished streaming recommendations with metadata: resource={},"
            + " recommendationCount={}",
        analysisOrDataset, recommendationCount);
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

  @Nonnull
  private RecommendationWithMetadata generateRecommendationWithMetadata(AlertRecommendation ar) {
    var recommendation = ar.toRecommendation();
    var metadata = ar.toMetadata();

    return RecommendationWithMetadata
        .newBuilder()
        .setRecommendation(recommendation)
        .setMetadata(metadata)
        .build();
  }
}
