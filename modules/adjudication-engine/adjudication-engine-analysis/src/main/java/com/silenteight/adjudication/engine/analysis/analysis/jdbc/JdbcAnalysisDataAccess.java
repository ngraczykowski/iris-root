package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisDataAccess;
import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcAnalysisDataAccess implements AnalysisDataAccess {

  private final SelectAnalysisAgentConfigQuery selectAnalysisAgentConfigQuery;

  private final SelectAnalysisByPendingRecommendationMatches
      selectAnalysisByPendingRecommendationMatches;

  private final SelectFeatureVectorElementsQuery selectFeatureVectorElementsQuery;

  @Override
  public List<String> findAgentConfigsByAnalysisId(long analysisId) {
    return selectAnalysisAgentConfigQuery.execute(analysisId);
  }

  @Override
  public List<Long> findByPendingRecommendationMatchIds(List<Long> matchIds) {
    return selectAnalysisByPendingRecommendationMatches.execute(matchIds);
  }

  @Override
  public PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return selectFeatureVectorElementsQuery.execute(analysisId);
  }
}
