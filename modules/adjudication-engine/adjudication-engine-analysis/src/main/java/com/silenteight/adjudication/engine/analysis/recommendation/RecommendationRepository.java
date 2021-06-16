package com.silenteight.adjudication.engine.analysis.recommendation;

import org.springframework.data.repository.Repository;

import java.util.List;
import javax.annotation.Nonnull;

interface RecommendationRepository extends Repository<RecommendationEntity, Long> {

  @Nonnull
  List<RecommendationEntity> saveAll(Iterable<RecommendationEntity> recommendations);

  @Nonnull
  RecommendationEntity getById(long recommendationId);
}
