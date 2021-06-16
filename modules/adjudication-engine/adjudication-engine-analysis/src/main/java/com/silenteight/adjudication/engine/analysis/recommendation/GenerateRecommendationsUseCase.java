package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class GenerateRecommendationsUseCase {

  private final AlertSolvingClient client;
  private final RecommendationDataAccess recommendationDataAccess;
  private final CreateRecommendationsUseCase createRecommendationsUseCase;

  Optional<RecommendationsGenerated> generateRecommendations(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    var recommendations = new ArrayList<RecommendationInfo>();

    if (log.isDebugEnabled()) {
      log.debug("Starting generating recommendations: analysis={}", analysisName);
    }

    do {
      var request = createRequest(analysisId);

      if (request.isEmpty()) {
        log.debug("No more alerts pending recommendation: analysis={}", analysisName);
        break;
      }

      var response = client.batchSolveAlerts(request.get());

      recommendations.addAll(createRecommendations(analysisId, response));
    } while (true);

    if (recommendations.isEmpty()) {
      log.info("No recommendations generated: analysis={}", analysisName);
      return Optional.empty();
    }

    log.info("Finished generating recommendations: analysis={}, recommendationCount={}",
        analysisName, recommendations.size());

    return Optional.of(RecommendationsGenerated.newBuilder()
        .setAnalysis(analysisName)
        .addAllRecommendationInfos(recommendations)
        .build());
  }

  private Optional<BatchSolveAlertsRequest> createRequest(long analysisId) {
    var pendingAlerts = recommendationDataAccess.selectPendingAlerts(analysisId);

    if (pendingAlerts.isEmpty()) {
      log.debug("No pending alerts: analysis=analysis/{}", analysisId);
      return Optional.empty();
    }

    if (log.isDebugEnabled()) {
      log.debug("Fetched pending alerts: analysis=analysis/{}, pendingCount={}",
          analysisId, pendingAlerts.size());
    }

    return Optional.of(pendingAlerts.toBatchSolveAlertsRequest());
  }

  private List<RecommendationInfo> createRecommendations(
      long analysisId, BatchSolveAlertsResponse response) {

    var alertSolutions = response
        .getSolutionsList()
        .stream()
        .map(GenerateRecommendationsUseCase::makeAlertSolution)
        .collect(toList());

    return createRecommendationsUseCase.createRecommendations(analysisId, alertSolutions);
  }

  private static AlertSolution makeAlertSolution(SolveAlertSolutionResponse response) {
    var alertId = ResourceName.create(response.getAlertName()).getLong("alerts");

    return new AlertSolution(alertId, response.getAlertSolution());
  }
}
