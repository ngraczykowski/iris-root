package com.silenteight.adjudication.engine.analysis.matchrecommendation;


import org.springframework.data.repository.Repository;

interface MatchRecommendationRepository extends Repository<MatchRecommendationEntity, Long> {

  Iterable<MatchRecommendationEntity> saveAll(Iterable<MatchRecommendationEntity> entities);
}
