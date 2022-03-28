package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class GetPolicyAndFeatureVectorElementsUseCase {

  @NonNull
  private final AnalysisDataAccess analysisDataAccess;

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "analysis" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return analysisDataAccess.getPolicyAndFeatureVectorElements(analysisId);
  }
}
