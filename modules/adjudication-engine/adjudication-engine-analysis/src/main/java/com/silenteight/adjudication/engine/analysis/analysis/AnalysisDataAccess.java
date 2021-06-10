package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;

import java.util.List;

public interface AnalysisDataAccess {

  List<String> findAgentConfigsByAnalysisId(long analysisId);

  List<Long> findByPendingRecommendationMatchIds(List<Long> matchIds);

  PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId);
}
