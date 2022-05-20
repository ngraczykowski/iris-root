package com.silenteight.adjudication.engine.analysis.matchrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.MatchRecommendationDataAccess;
import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class JdbMatchRecommendationDataAccess implements MatchRecommendationDataAccess {

  private final SelectPendingMatchesQuery selectPendingMatchesQuery;

  @Override
  public List<PendingMatch> selectPendingMatches(long analysisId) {
    return selectPendingMatchesQuery.execute(analysisId);
  }
}
