package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetPolicyAndFeatureVectorElementsUseCase {

  @NonNull
  private final AnalysisDataAccess analysisDataAccess;

  PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return analysisDataAccess.getPolicyAndFeatureVectorElements(analysisId);
  }
}
