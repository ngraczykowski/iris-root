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

  @Modifying
  @Query(value =
      "UPDATE scb_recommendation SET recom_status = :status "
          + "WHERE scb_recommendation_id = (SELECT scb_recommendation_id "
          + "FROM scb_recommendation WHERE system_id = :systemId AND watchlist_id = :watchlistId "
          + "ORDER BY created_at DESC LIMIT 1)", nativeQuery = true)
  void updateRecomStatus(String systemId, String watchlistId, String status);
}