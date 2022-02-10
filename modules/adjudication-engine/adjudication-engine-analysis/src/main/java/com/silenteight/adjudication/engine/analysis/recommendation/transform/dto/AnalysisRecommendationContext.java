package com.silenteight.adjudication.engine.analysis.recommendation.transform.dto;

import lombok.Value;

import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

import java.util.List;

@Value
public class AnalysisRecommendationContext {

  List<MatchContext> matches;
  long analysisId;
  long recommendationId;
  long alertId;
  long[] matchIds;
}
