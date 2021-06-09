package com.silenteight.adjudication.engine.analysis.matchsolution.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;
import com.silenteight.adjudication.engine.analysis.matchsolution.AnalysisFeatureVectorElementsProvider;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AnalysisFeatureVectorElementsProviderImpl
    implements AnalysisFeatureVectorElementsProvider {

  private final AnalysisFacade analysisFacade;

  @Override
  public PolicyAndFeatureVectorElements get(long analysisId) {
    return analysisFacade.getAnalysisPolicyAndFeatureVectorElements(analysisId);
  }
}
