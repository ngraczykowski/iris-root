package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;

@RequiredArgsConstructor
class GetPolicyAndFeatureVectorElementsUseCase {

  private final AnalysisDataAccess analysisDataAccess;

  PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return analysisDataAccess.getPolicyAndFeatureVectorElements(analysisId);
  }
}
