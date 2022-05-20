package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("commentinput.handlePendingRecommendationsUseCase")
@Slf4j
class HandlePendingRecommendationsUseCase {

  private final FetchAllMissingCommentInputsUseCase fetchAllMissingCommentInputsUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  void handlePendingRecommendations(PendingRecommendations pendingRecommendations) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Handling pending recommendations: analysisList={}",
          pendingRecommendations.getAnalysisList());
    }

    pendingRecommendations
        .getAnalysisList()
        .forEach(fetchAllMissingCommentInputsUseCase::fetchAllMissingCommentInputsValues);
  }
}
