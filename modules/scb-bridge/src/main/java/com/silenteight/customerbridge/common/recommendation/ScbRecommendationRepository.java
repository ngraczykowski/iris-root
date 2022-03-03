package com.silenteight.customerbridge.common.recommendation;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface ScbRecommendationRepository extends Repository<ScbRecommendation, Long> {

  // @formatter:off
  Optional<ScbRecommendation>
      findFirstBySystemIdAndDiscriminatorAndWatchlistIdIsNullOrderByRecommendedAtDesc(
      String systemId, String discriminator);
  // @formatter:on

  // @formatter:off
  Optional<ScbRecommendation>
      findFirstBySystemIdAndWatchlistIdIsNullOrderByRecommendedAtDesc(String systemId);
  // @formatter:on

  Collection<ScbRecommendation> findAll();

  void save(ScbRecommendation scbRecommendation);

  Optional<ScbRecommendation> findFirstBySystemIdOrderByRecommendedAtDesc(String systemId);
}