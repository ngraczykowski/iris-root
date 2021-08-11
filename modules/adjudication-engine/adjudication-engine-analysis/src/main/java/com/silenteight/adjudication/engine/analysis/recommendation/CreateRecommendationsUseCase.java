package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.RecommendationResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateRecommendationsUseCase {

  private final RecommendationDataAccess recommendationDataAccess;
  private final PendingRecommendationFacade pendingRecommendationFacade;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  @Transactional
  List<RecommendationInfo> createRecommendations(
      long analysisId, List<AlertSolution> alertSolutions) {

    var recommendations = createInsertRequests(analysisId, alertSolutions);
    var savedRecommendations = recommendationDataAccess.insertAlertRecommendation(recommendations);

    pendingRecommendationFacade.removeSolvedPendingRecommendation();

    if (log.isDebugEnabled()) {
      log.debug("Saved recommendations: analysis=analysis/{}, recommendationCount={}",
          analysisId, recommendations.size());
    }

    return savedRecommendations
        .stream()
        .map(RecommendationResponse::toRecommendationInfo)
        .collect(toList());
  }

  @NotNull
  private static List<InsertRecommendationRequest> createInsertRequests(
      long analysisId, List<AlertSolution> alertSolutions) {

    return alertSolutions
        .stream()
        .map(a -> InsertRecommendationRequest.fromAlertSolution(analysisId, a))
        .collect(toList());
  }
}
