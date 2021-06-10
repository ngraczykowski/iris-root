package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class SolveAnalysisMatchesUseCase {

  private final AnalysisFeatureVectorElementsProvider analysisFeatureVectorElementsProvider;
  private final SolveMatchesUseCase solveMatchesUseCase;

  List<String> solveAnalysisMatches(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    var featureVectorElements = analysisFeatureVectorElementsProvider.get(analysisId);
    var solveMatchesRequest = new SolveMatchesRequest(
        analysisId, featureVectorElements.getPolicy(), featureVectorElements.toFeatureCollection());

    return solveMatchesUseCase.solveMatches(solveMatchesRequest);
  }
}
