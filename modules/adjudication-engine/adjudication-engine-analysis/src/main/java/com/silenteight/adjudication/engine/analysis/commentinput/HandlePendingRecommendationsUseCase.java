package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("commentinput.handlePendingRecommendationsUseCase")
@Slf4j
class HandlePendingRecommendationsUseCase {

  private final FetchAllMissingCommentInputsUseCase fetchAllMissingCommentInputsUseCase;

  void handlePendingRecommendations(PendingRecommendations pendingRecommendations) {
    if (log.isDebugEnabled()) {
      log.debug("Handling pending recommendations: analysis={}",
          pendingRecommendations.getAnalysisList());
    }

    pendingRecommendations
        .getAnalysisList()
        .forEach(fetchAllMissingCommentInputsUseCase::fetchAllMissingCommentInputsValues);
  }
}
