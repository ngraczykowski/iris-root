package com.silenteight.adjudication.engine.analysis.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.dataset.AnalysisDatasetFacade;
import com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

@SuppressWarnings("java:S1200")
@RequiredArgsConstructor
@Service
class AnalysisService {

  @NonNull
  private final AnalysisFacade analysisFacade;

  private final AnalysisDatasetFacade analysisDatasetFacade;

  private final MatchSolutionFacade matchSolutionFacade;

  private final RecommendationFacade recommendationFacade;

  Analysis createAnalysis(CreateAnalysisRequest request) {
    return analysisFacade.createAndGetAnalysis(request.getAnalysis());
  }

  AnalysisDataset addDataset(AddDatasetRequest request) {
    return analysisDatasetFacade.batchAddAndListDataset(
        request.getAnalysis(), singletonList(request.getDataset())).get(0);
  }

  BatchAddDatasetsResponse batchAddDatasets(BatchAddDatasetsRequest request) {
    List<AnalysisDataset> datasets = analysisDatasetFacade.batchAddAndListDataset(
        request.getAnalysis(), request.getDatasetsList());
    return BatchAddDatasetsResponse.newBuilder().addAllAnalysisDatasets(datasets).build();
  }

  AnalysisAlert addAlert(AddAlertRequest addAlertRequest) {
    return analysisFacade
        .batchAddAlert(
            addAlertRequest.getAnalysis(),
            singletonList(addAlertRequest.getAnalysisAlert()))
        .get(0);
  }

  BatchAddAlertsResponse batchAddAlerts(BatchAddAlertsRequest batchAddAlertsRequest) {
    var alerts = analysisFacade
        .batchAddAlert(
            batchAddAlertsRequest.getAnalysis(),
            batchAddAlertsRequest.getAnalysisAlertsList());
    return BatchAddAlertsResponse.newBuilder().addAllAnalysisAlerts(alerts).build();
  }

  Analysis getAnalysis(GetAnalysisRequest request) {
    return analysisFacade.getAnalysis(request.getAnalysis());
  }

  /**
   * @deprecated Use {@link RecommendationService}.
   */
  @Deprecated
  Recommendation getRecommendation(GetRecommendationRequest request) {
    return recommendationFacade.getRecommendation(request.getRecommendation());
  }

  MatchSolution getMatchSolution(GetMatchSolutionRequest request) {
    return matchSolutionFacade.getMatchSolution(request.getMatchSolution());
  }

  /**
   * @deprecated Use {@link RecommendationService}.
   */
  @Deprecated
  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> onNext) {

    var resource = request.getDataset().isEmpty() ? request.getAnalysis() : request.getDataset();

    recommendationFacade.streamRecommendations(resource, onNext);
  }
}
