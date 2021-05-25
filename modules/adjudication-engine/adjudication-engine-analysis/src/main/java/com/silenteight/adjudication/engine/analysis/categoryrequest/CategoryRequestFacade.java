package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryRequestFacade {

  private final FetchAllMissingCategoryValuesUseCase useCase;

  public void handlePendingRecommendations(PendingRecommendations pendingRecommendations) {
    pendingRecommendations.getAnalysisList().forEach(useCase::fetchAllMissingCategoryValues);

    if (log.isDebugEnabled()) {
      log.debug(
          "Handled pending recommendations: analysis={}",
          pendingRecommendations.getAnalysisList());
    }
  }
}
