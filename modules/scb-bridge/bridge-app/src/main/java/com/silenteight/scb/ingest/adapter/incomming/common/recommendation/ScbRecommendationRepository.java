package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface ScbRecommendationRepository extends Repository<ScbRecommendation, Long> {

  Collection<ScbRecommendation> findAll();

  void save(ScbRecommendation scbRecommendation);

  Optional<ScbRecommendation> findFirstBySystemIdOrderByRecommendedAtDesc(String systemId);
}