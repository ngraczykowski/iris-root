package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
@RequiredArgsConstructor
class HandleCommentInputsUpdatedUseCase {

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;

  List<RecommendationsGenerated> handleCommentInputsUpdated(
      CommentInputsUpdated commentInputsUpdated) {

    return commentInputsUpdated
        .getAnalysisList()
        .stream()
        .map(generateRecommendationsUseCase::generateRecommendations)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toUnmodifiableList());
  }
}
