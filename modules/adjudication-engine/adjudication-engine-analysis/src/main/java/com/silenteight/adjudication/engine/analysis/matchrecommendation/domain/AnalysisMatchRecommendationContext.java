package com.silenteight.adjudication.engine.analysis.matchrecommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;

@Value
@Builder
public class AnalysisMatchRecommendationContext {

  MatchContext matchContext;
  long analysisId;
  long recommendationId;
  long alertId;
  long matchId;
}
