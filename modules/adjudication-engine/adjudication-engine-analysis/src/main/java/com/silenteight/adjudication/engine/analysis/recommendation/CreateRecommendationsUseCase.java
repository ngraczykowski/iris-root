package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateRecommendationsUseCase {

  private final RecommendationRepository repository;
  private final PendingRecommendationFacade pendingRecommendationFacade;

  @Transactional
  List<RecommendationInfo> createRecommendations(
      long analysisId, List<AlertSolution> alertSolutions) {

    var recommendations = createRecommendationEntities(analysisId, alertSolutions);
    var savedRecommendations = repository.saveAll(recommendations);

    pendingRecommendationFacade.removeSolvedPendingRecommendation();

    if (log.isDebugEnabled()) {
      log.debug("Saved recommendations: analysis=analysis/{}, recommendationCount={}",
          analysisId, recommendations.size());
    }

    return savedRecommendations
        .stream()
        .map(RecommendationEntity::toRecommendationInfo)
        .collect(toList());
  }

  @NotNull
  private List<RecommendationEntity> createRecommendationEntities(
      long analysisId, List<AlertSolution> alertSolutions) {

    return alertSolutions
        .stream()
        .map(a -> RecommendationEntity.fromAlertSolution(analysisId, a))
        .collect(toList());
  }
}
