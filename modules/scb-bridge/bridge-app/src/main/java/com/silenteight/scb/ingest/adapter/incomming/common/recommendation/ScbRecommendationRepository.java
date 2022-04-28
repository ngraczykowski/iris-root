package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ScbRecommendationRepository extends CrudRepository<ScbRecommendation, Long> {

  Optional<ScbRecommendation> findFirstBySystemIdOrderByRecommendedAtDesc(String systemId);

  @Modifying
  @Query("DELETE FROM ScbRecommendation")
  void deleteAll();
}