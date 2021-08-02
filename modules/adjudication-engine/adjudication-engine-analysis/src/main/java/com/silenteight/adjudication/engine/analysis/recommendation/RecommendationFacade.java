package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

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

  public List<RecommendationsGenerated> handleCommentInputsUpdated(
      CommentInputsUpdated commentInputsUpdated) {

    return handleCommentInputsUpdatedUseCase.handleCommentInputsUpdated(commentInputsUpdated);
  }

  public Optional<RecommendationsGenerated> handleMatchesSolved(MatchesSolved matchesSolved) {
    return handleMatchesSolvedUseCase.handleMatchesSolved(matchesSolved);
  }

  public Recommendation getRecommendation(String recommendationName) {
    return getRecommendationUseCase.getRecommendation(recommendationName);
  }

  public RecommendationMetadata getRecommendationMetadata(String metadataName) {
    return getRecommendationUseCase.getRecommendationMetadata(metadataName);
  }

  public RecommendationWithMetadata getRecommendationWithMetadata(String recommendation) {
    return getRecommendationUseCase.getRecommendationWithMetadata(recommendation);
  }

  public void streamRecommendations(
      String analysisOrDataset, Consumer<Recommendation> recommendationConsumer) {

    streamRecommendationsUseCase.streamRecommendations(analysisOrDataset, recommendationConsumer);
  }

  public void streamRecommendationsWithMetadata(
      String analysisOrDataset, Consumer<RecommendationWithMetadata> consumer) {

    streamRecommendationsUseCase.streamRecommendationsWithMetadata(
        analysisOrDataset, consumer);
  }
}
