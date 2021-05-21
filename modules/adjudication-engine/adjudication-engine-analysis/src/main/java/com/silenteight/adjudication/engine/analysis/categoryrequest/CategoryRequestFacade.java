package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryRequestFacade {

  private final MissingCategoryValueUseCase useCase;

  public void handlePendingRecommendation(PendingRecommendations pendingRecommendations) {
    pendingRecommendations.getAnalysisList().stream().forEach(s -> {
          log.info("Handle missing category values for analysis:{}", s);
          useCase.handleMissingCategoryValues(ResourceName.create(s).getLong("analysis"));
        }
    );
    log.info(
        "Finished pending recommendations notify by MatchCategoriesUpdated event with {}",
        pendingRecommendations.getAnalysisList());
  }
}
