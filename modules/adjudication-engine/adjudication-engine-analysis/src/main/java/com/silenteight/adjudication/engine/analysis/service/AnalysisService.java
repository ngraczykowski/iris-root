package com.silenteight.adjudication.engine.analysis.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
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

  private final RecommendationFacade recommendationFacade;

  private final MatchSolutionFacade matchSolutionFacade;

  Analysis createAnalysis(CreateAnalysisRequest request) {
    return analysisFacade.createAndGetAnalysis(request.getAnalysis());
  }

  AnalysisDataset addDataset(AddDatasetRequest request) {
    return analysisFacade.addDatasets(
        request.getAnalysis(), singletonList(request.getDataset())).get(0);
  }

  BatchAddDatasetsResponse batchAddDatasets(BatchAddDatasetsRequest request) {
    List<AnalysisDataset> datasets = analysisFacade.addDatasets(
        request.getAnalysis(), request.getDatasetsList());
    return BatchAddDatasetsResponse.newBuilder().addAllAnalysisDatasets(datasets).build();
  }

  Analysis getAnalysis(GetAnalysisRequest request) {
    return analysisFacade.getAnalysis(request.getAnalysis());
  }

  Recommendation getRecommendation(GetRecommendationRequest request) {
    return recommendationFacade.getRecommendation(request.getRecommendation());
  }

  MatchSolution getMatchSolution(GetMatchSolutionRequest request) {
    return matchSolutionFacade.getMatchSolution(request.getMatchSolution());
  }

  void streamRecommendations(
      StreamRecommendationsRequest request, Consumer<Recommendation> onNext) {

    recommendationFacade.streamRecommendations(request, onNext);
  }
}
