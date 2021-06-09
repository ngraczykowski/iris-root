package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class SolveAnalysisMatchesUseCase {

  private final AnalysisFeatureVectorElementsProvider analysisFeatureVectorElementsProvider;
  private final SolveMatchesUseCase solveMatchesUseCase;

  void solveAnalysisMatches(long analysisId) {
    var featureVectorElements = analysisFeatureVectorElementsProvider.get(analysisId);
    var solveMatchesRequest = new SolveMatchesRequest(
        analysisId, featureVectorElements.getPolicy(), featureVectorElements.toFeatureCollection());

    solveMatchesUseCase.solveMatches(solveMatchesRequest);
  }
}
