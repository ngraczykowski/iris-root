package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisDataAccess;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAttachmentFlags;
import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcAnalysisDataAccess implements AnalysisDataAccess {

  private final SelectAnalysisAgentConfigQuery selectAnalysisAgentConfigQuery;
  private final SelectAnalysisAttachmentFlagsQuery selectAnalysisAttachmentFlagsQuery;

  private final SelectAnalysisByPendingRecommendationMatches
      selectAnalysisByPendingRecommendationMatches;

  private final SelectFeatureVectorElementsQuery selectFeatureVectorElementsQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<String> findAgentConfigsByAnalysisId(long analysisId) {
    return selectAnalysisAgentConfigQuery.execute(analysisId);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<Long> findByPendingRecommendationMatchIds(List<Long> matchIds) {
    return selectAnalysisByPendingRecommendationMatches.execute(matchIds);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public PolicyAndFeatureVectorElements getPolicyAndFeatureVectorElements(long analysisId) {
    return selectFeatureVectorElementsQuery.execute(analysisId);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public AnalysisAttachmentFlags getAnalysisAttachmentFlags(long analysisId) {
    return selectAnalysisAttachmentFlagsQuery.execute(analysisId);
  }

}
