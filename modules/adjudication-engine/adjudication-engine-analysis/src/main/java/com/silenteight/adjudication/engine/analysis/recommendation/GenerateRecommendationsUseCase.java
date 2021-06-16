package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;
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
class GenerateRecommendationsUseCase {

  private final AlertSolvingClient client;
  private final RecommendationDataAccess recommendationDataAccess;
  private final CreateRecommendationsUseCase createRecommendationsUseCase;

  Optional<RecommendationsGenerated> generateRecommendations(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    var recommendations = new ArrayList<RecommendationInfo>();

    do {
      var request = createRequest(analysisId);

      if (request.isEmpty())
        break;

      var response = client.batchSolveAlerts(request.get());

      recommendations.addAll(createRecommendations(analysisId, response));
    } while (true);

    if (recommendations.isEmpty())
      return Optional.empty();

    return Optional.of(RecommendationsGenerated.newBuilder()
        .setAnalysis(analysisName)
        .addAllRecommendationInfos(recommendations)
        .build());
  }

  private Optional<BatchSolveAlertsRequest> createRequest(long analysisId) {
    var pendingAlerts = recommendationDataAccess.selectPendingAlerts(analysisId);

    if (pendingAlerts.isEmpty())
      return Optional.empty();

    return Optional.of(
        BatchSolveAlertsRequest
            .newBuilder()
            .addAllAlerts(pendingAlerts.stream().map(PendingAlert::toAlert).collect(toList()))
            .build()
    );
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
