package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;

public interface AnalysisFeatureVectorElementsProvider {

  PolicyAndFeatureVectorElements get(long analysisId);
}
