package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryRequestFacade {

  private final FetchAllMissingCategoryValuesUseCase fetchAllMissingCategoryValuesUseCase;

  private final HandlePendingRecommendationsUseCase handlePendingRecommendationsUseCase;


  public MatchCategoriesUpdated fetchAllMissingCategoryValues(String analysis) {
    return fetchAllMissingCategoryValuesUseCase.fetchAllMissingCategoryValues(analysis);
  }

  public List<MatchCategoriesUpdated> handlePendingRecommendations(
      PendingRecommendations pendingRecommendations) {

    return handlePendingRecommendationsUseCase.handlePendingRecommendations(pendingRecommendations);
  }
}
