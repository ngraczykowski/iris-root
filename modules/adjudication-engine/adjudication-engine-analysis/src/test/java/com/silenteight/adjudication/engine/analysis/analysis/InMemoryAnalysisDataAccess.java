package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAttachmentFlags;
import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;

import java.util.List;

class InMemoryAnalysisDataAccess implements AnalysisDataAccess {

  @Override
  public List<String> findAgentConfigsByAnalysisId(long analysisId) {
    return null;
  }

  @Override
  public List<Long> findByPendingRecommendationMatchIds(List<Long> matchIds) {
    return null;
  }

  @Override
  public PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(
      long analysisId) {
    return null;
  }

  @Override
  public AnalysisAttachmentFlags getAnalysisAttachmentFlags(
      long analysisId) {
    return new AnalysisAttachmentFlags(true, true);
  }
}
