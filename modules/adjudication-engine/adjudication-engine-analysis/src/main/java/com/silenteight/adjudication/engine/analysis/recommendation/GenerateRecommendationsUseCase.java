package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlert;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.PendingAlerts;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class GenerateRecommendationsUseCase {

  private final AlertSolvingClient client;
  private final RecommendationDataAccess recommendationDataAccess;
  private final AnalysisFacade analysisFacade;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  List<RecommendationInfo> generateRecommendations(
      String analysisName,
      Function<SaveRecommendationRequest, List<RecommendationInfo>> saveRecommendation) {

    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    var recommendationInfos = new ArrayList<RecommendationInfo>();

    log.debug("Starting generating recommendations: analysis={}", analysisName);
    do {
      var pendingAlerts = recommendationDataAccess.selectPendingAlerts(analysisId);
      var request = createRequest(analysisId, pendingAlerts);

      if (request.isEmpty()) {
        log.debug("No more alerts pending recommendation: analysis={}", analysisName);
        break;
      }

      var response = client.batchSolveAlerts(request.get());

      var alertSolutions = createAlertSolutions(pendingAlerts, response);
      recommendationInfos.addAll(
          saveRecommendation.apply(new SaveRecommendationRequest(analysisId, alertSolutions)));

      if (alertSolutions.isEmpty()) {
        log.info("No recommendations generated: analysis={}", analysisName);
      }
    } while (true);

    log.info("Finished generating recommendations: analysis={}, recommendationCount={}",
        analysisName, recommendationInfos.size());

    return recommendationInfos;
  }

  @NotNull
  private static List<AlertSolution> createAlertSolutions(
      PendingAlerts alerts, BatchSolveAlertsResponse response) {

    return response
        .getSolutionsList()
        .stream()
        .map(s -> createAlertSolution(alerts, s))
        .collect(toList());
  }

  private Optional<BatchSolveAlertsRequest> createRequest(
      long analysisId, PendingAlerts pendingAlerts) {

    if (pendingAlerts.isEmpty()) {
      log.debug("No pending alerts: analysis=analysis/{}", analysisId);
      return Optional.empty();
    }

    if (log.isDebugEnabled()) {
      log.debug("Fetched pending alerts: analysis=analysis/{}, pendingCount={}",
          analysisId, pendingAlerts.size());
    }

    var strategy = analysisFacade.getAnalysisStrategy(analysisId);
    return Optional.of(pendingAlerts.toBatchSolveAlertsRequest(strategy));
  }

  private static AlertSolution createAlertSolution(
      PendingAlerts alerts, SolveAlertSolutionResponse response) {

    var alertId = ResourceName.create(response.getAlertName()).getLong("alerts");
    var pendingAlert = getPendingAlert(alerts, alertId);
    return AlertSolution.builder()
        .alertId(alertId)
        .recommendedAction(response.getAlertSolution())
        .matchIds(pendingAlert.getMatchIds())
        .matchContexts(pendingAlert.getMatchContexts())
        .build();
  }

  private static PendingAlert getPendingAlert(PendingAlerts alerts, long alertId) {
    return alerts.getById(alertId)
        .orElseThrow(() -> new PendingAlertSolutionNotMatchedException(alertId));
  }

  static class PendingAlertSolutionNotMatchedException extends IllegalStateException {

    private static final long serialVersionUID = -1681350458025837494L;

    PendingAlertSolutionNotMatchedException(long alertId) {
      super("Could not match Pending Alert with the Solution, alertId=" + alertId);
    }
  }
}
