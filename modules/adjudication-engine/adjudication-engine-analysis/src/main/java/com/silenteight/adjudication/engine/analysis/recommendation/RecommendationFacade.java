package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class RecommendationFacade {

  private final HandleCommentInputsUpdatedUseCase handleCommentInputsUpdatedUseCase;

  private final HandleMatchesSolvedUseCase handleMatchesSolvedUseCase;

  private final GetRecommendationUseCase getRecommendationUseCase;

  private final StreamRecommendationsUseCase streamRecommendationsUseCase;

  private final CreateRecommendationsUseCase createRecommendationsUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<RecommendationsGenerated> handleCommentInputsUpdated(
      CommentInputsUpdated commentInputsUpdated) {

    return handleCommentInputsUpdatedUseCase.handleCommentInputsUpdated(commentInputsUpdated);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Optional<RecommendationsGenerated> handleMatchesSolved(MatchesSolved matchesSolved) {
    return handleMatchesSolvedUseCase.handleMatchesSolved(matchesSolved);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Recommendation getRecommendation(String recommendationName) {
    return getRecommendationUseCase.getRecommendation(recommendationName);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public RecommendationMetadata getRecommendationMetadata(String metadataName) {
    return getRecommendationUseCase.getRecommendationMetadata(metadataName);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public RecommendationWithMetadata getRecommendationWithMetadata(String recommendation) {
    return getRecommendationUseCase.getRecommendationWithMetadata(recommendation);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void streamRecommendations(
      String analysisOrDataset, Consumer<Recommendation> recommendationConsumer) {

    streamRecommendationsUseCase.streamRecommendations(analysisOrDataset, recommendationConsumer);
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void streamRecommendationsWithMetadata(
      String analysisOrDataset, Consumer<RecommendationWithMetadata> consumer) {

    streamRecommendationsUseCase.streamRecommendationsWithMetadata(
        analysisOrDataset, consumer);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<RecommendationInfo> createRecommendations(SaveRecommendationRequest request) {
    return createRecommendationsUseCase.createRecommendations(request);
  }
}
