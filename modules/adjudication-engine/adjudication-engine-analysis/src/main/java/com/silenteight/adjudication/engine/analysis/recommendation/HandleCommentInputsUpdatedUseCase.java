package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.CommentInputsUpdated;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
@RequiredArgsConstructor
@Slf4j
class HandleCommentInputsUpdatedUseCase {

  private final GenerateAndSaveRecommendationUseCase generateAndSaveRecommendationUseCase;

  List<RecommendationsGenerated> handleCommentInputsUpdated(
      CommentInputsUpdated commentInputsUpdated) {

    if (log.isDebugEnabled()) {
      log.debug("Handling comment inputs updated: analysisList={}",
          commentInputsUpdated.getAnalysisList());
    }

    return commentInputsUpdated
        .getAnalysisList()
        .stream()
        .map(generateAndSaveRecommendationUseCase::generateAndSaveRecommendations)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toUnmodifiableList());
  }
}
