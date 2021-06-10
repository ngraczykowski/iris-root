package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("commentinput.handlePendingRecommendationsUseCase")
class HandlePendingRecommendationsUseCase {

  private final FetchAllMissingCommentInputsUseCase fetchAllMissingCommentInputsUseCase;

  void handlePendingRecommendations(PendingRecommendations pendingRecommendations) {
    pendingRecommendations
        .getAnalysisList()
        .forEach(fetchAllMissingCommentInputsUseCase::fetchAllMissingCommentInputsValues);
  }
}
