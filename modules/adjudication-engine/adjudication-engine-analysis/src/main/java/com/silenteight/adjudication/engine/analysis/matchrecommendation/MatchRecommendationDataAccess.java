package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;

import java.util.List;

public interface MatchRecommendationDataAccess {

  List<PendingMatch> selectPendingMatches(long analysisId);
}
