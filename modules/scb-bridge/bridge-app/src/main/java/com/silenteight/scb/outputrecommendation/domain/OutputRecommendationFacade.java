package com.silenteight.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutputRecommendationFacade {

  private final RecommendationsProcessor recommendationsProcessor;

  public void prepareCompletedBatchRecommendations(PrepareRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchCompleted(command);
  }

  public void prepareErrorBatchRecommendations(PrepareErrorRecommendationResponseCommand command) {
    recommendationsProcessor.processBatchError(command);
  }
}
