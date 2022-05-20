package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentInputFacade {

  private final HandlePendingRecommendationsUseCase handlePendingRecommendationsUseCase;

  public void handlePendingRecommendations(PendingRecommendations pendingRecommendations) {
    handlePendingRecommendationsUseCase.handlePendingRecommendations(pendingRecommendations);
  }
}
