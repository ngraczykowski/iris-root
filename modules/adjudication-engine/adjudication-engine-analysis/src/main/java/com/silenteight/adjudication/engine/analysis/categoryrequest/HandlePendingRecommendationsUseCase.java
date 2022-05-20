package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Slf4j
@Service("categoryrequest.handlePendingRecommendationsUseCase")
class HandlePendingRecommendationsUseCase {

  private final FetchAllMissingCategoryValuesUseCase fetchAllMissingCategoryValuesUseCase;

  List<MatchCategoriesUpdated> handlePendingRecommendations(
      PendingRecommendations pendingRecommendations) {

    var result = pendingRecommendations
        .getAnalysisList()
        .stream()
        .map(fetchAllMissingCategoryValuesUseCase::fetchAllMissingCategoryValues)
        .collect(toUnmodifiableList());

    if (log.isDebugEnabled()) {
      log.debug(
          "Fetched missing category values: analysisList={}",
          pendingRecommendations.getAnalysisList());
    }

    return result;
  }
}
