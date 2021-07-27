package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetPolicyAndFeatureVectorElementsUseCase {

  @NonNull
  private final AnalysisDataAccess analysisDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return analysisDataAccess.getPolicyAndFeatureVectorElements(analysisId);
  }
}
