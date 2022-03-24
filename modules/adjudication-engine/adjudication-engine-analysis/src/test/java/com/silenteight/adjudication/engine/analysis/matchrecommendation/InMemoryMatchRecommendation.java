package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFixture.createMatchContext;

class InMemoryMatchRecommendation
    implements MatchRecommendationDataAccess, MatchRecommendationRepository {

  private ObjectMapper mapper = new ObjectMapper();
  private List<PendingMatch> pendingMatches =
      List.of(createPendingMatch(), createPendingMatch(), createPendingMatch());

  @Override
  public List<PendingMatch> selectPendingMatches(long analysisId) {
    return pendingMatches;
  }

  private PendingMatch createPendingMatch() {
    return PendingMatch
        .builder()
        .matchId(1)
        .alertId(1)
        .matchSolution(FeatureVectorSolution.FEATURE_VECTOR_SOLUTION_UNSPECIFIED)
        .matchContexts(mapper.valueToTree(createMatchContext()))
        .build();
  }

  @Override
  public Iterable<MatchRecommendationEntity> saveAll(
      Iterable<MatchRecommendationEntity> entities) {
    pendingMatches = List.of();
    for (var entity : entities) {
      entity.setId(1L);
    }
    return entities;
  }
}
